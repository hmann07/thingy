package com.thingy.network

import com.thingy.config.ConfigDataClass.ConfigData
import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.genome._
import com.thingy.neuron.Neuron
import play.api.libs.json._
import com.thingy.neuron.{Successor, Predecessor}
import com.thingy.neuron.Neuron.ConnectionConfig
import com.thingy.weight.Weight
import com.thingy.innovation._
import com.thingy.subnetwork.SubNetwork
import com.thingy.mutator.Mutator
import com.thingy.environment.Environment.Representation
import com.thingy.evaluator._
import scala.util.Random
import com.typesafe.config.ConfigFactory
import com.thingy.messages.Perceive

sealed trait NetworkState
case object Initialising extends NetworkState
case object Ready extends NetworkState
case object NetworkActive extends NetworkState
case object NetworkTest extends NetworkState
case object Mutating extends NetworkState



// State Data holder
final case class NetworkSettings(
		id: Int = 1, 
		genome:  NetworkGenome, 
		networkSchema: NetworkNodeSchema,
		rCount: Int = 0,
		expectedOutputs: Map[Int, Representation] = Map.empty, // Map[Batchid, Representation]
		actualOutputs: Map[Int, Map[Int, Double]] = Map.empty, // Map[Batchid, Map[OuputputId, ActualOutputValue]]
		evaluator: Evaluator = SoftmaxEvaluator()
		 )

object Network {

	implicit val config = ConfigFactory.load()
	

	// Messages it can receive
	final case class Signal(value: Double)
	final case class Mutate()
	case class Mutated()
	case class NetworkUpdate(f: GenomeIO)
	case class Done(evaluator: Evaluator, genome: NetworkGenome)
	case class Completed()
    // an override of props to allow Actor to take con structor args.
	// Network should take a genome and create a number of sub networks.

	def props(name: String, 
			  networkGenome: NetworkGenome, 
			  innovation: ActorRef, 
			  environment: ActorRef, 
			  configData: ConfigData, 
			  startState: NetworkState, 
			  evaluator: Evaluator,
			  out: ActorRef = null): Props = {

		Props(classOf[Network], name, networkGenome, innovation, environment, configData, startState, evaluator, out)
	}
}

class Network(name: String, 
			  networkGenome: NetworkGenome, 
			  innovation: ActorRef, 
			  environment: ActorRef, 
			  configData: ConfigData, 
			  startState: NetworkState, 
			  evaluator: Evaluator, 
			  out: ActorRef = null) extends FSM[NetworkState, NetworkSettings] {

	import Network._
	

	log.debug("network: {} created with genome {}", name, networkGenome)


	// First create input neurons. Select these on the basis that they belong to layer 0.
	// Input neurons will not have any internal node structures so can be created as straight Neurons.
	// By giving the context the neurons will be created in this context.


	val nodeSchema: NetworkNodeSchema  = generateActors(NetworkNodeSchema(), networkGenome)
	
	// get a list of actors that are connected to inputs. we will send input directly. rather than put input actors.
	

	val mutator = new Mutator(configData)

	log.debug("Network actors are setup as {} ", nodeSchema)

	environment ! Perceive()

	startWith(startState, NetworkSettings(genome = networkGenome, networkSchema = nodeSchema, evaluator = evaluator))

	when(NetworkActive) {


		case Event(e: ConnectionConfig , t: NetworkSettings) =>
			log.info("network received connection settings, this shoud be turned off.")
			stay

		case Event(s: Network.Mutate, t: NetworkSettings) =>
			
			log.debug("mutating genome")
			
			innovation ! mutator.mutate(t.genome) 
			
			goto(Mutating) using t

		case Event(s: Neuron.Signal, t: NetworkSettings) =>

			log.debug("received signal of {}", s.value)
			nodeSchema.in.nodes.foreach {i =>
			 	i.actor ! s
				}

			stay using t

		case Event(r: Representation, t: NetworkSettings) =>

			log.debug("received representation of {} will generate batch {}", r.input, t.rCount)
			
			// Send the signals off into the network, send signal with a batch id (rCount)

			//REWORK (TO REDUCE THE NUMBER OF ACTORS IN LARGE FEATURE DOMAINS). THE GENERATED ACTORS MUST BE SELECTED ON THE BASIS THEY ARE CONNECTED TO AN INPUT. I.E THE WILL BE A SUB SELECTION
			// OF HIDDEN NODES (AND OR OUTPUT NODES) THAT ARE CONNECTED TO INPUTS DIRECTLY.
			//  NOTE: NOT ALL HIDDEN NODES ARE CONNECTED TO AN INPUT.
			// SO WE MUUST COLLECT NOEURONS BY THE INPUT THEY ARE CONNECTED TO. THEN FOR EACH INPUT FEATURES SEND TO THE NEURONS RELATED TO THAT fEATURES.



			/*
			r.input.foreach(i => {
				nodeSchema.allNodes(i._1).actor ! Neuron.Signal(i._2, r.flags, t.rCount)
			})

			*/

			r.input.foreach(i => {
				nodeSchema.inNodes(i._1).foreach(a => a ! Neuron.Signal(i._2, r.flags, t.rCount))
			})


			val updateExpected = t.expectedOutputs + (t.rCount -> r)

			stay using t.copy(rCount = t.rCount + 1, expectedOutputs = updateExpected)

		case Event(s: Neuron.Output, t: NetworkSettings) =>

			log.debug("network received Output signal of {} from {}, reps processed: {}", s, sender(), t.expectedOutputs)

			// add signal to actual outputsReceived
			val existing = t.actualOutputs.getOrElse(s.batchId, Map.empty)
			val updateExisting = existing + (s.nodeId -> s.value)
			val updateOutputs = t.actualOutputs + (s.batchId -> updateExisting) 
			
			
			
			// check have we processed a representation complete?
			if(updateOutputs(s.batchId).size == t.expectedOutputs(s.batchId).expectedOutput.size) {
				//println("expected: " + t.expectedOutputs(s.batchId).expectedOutput + " received: " + updateOutputs(s.batchId) )
				// all outputs for batch received, we can evaluate them...
				val e = t.evaluator.evaluateIteration(t.expectedOutputs(s.batchId), updateOutputs(s.batchId))

				// now double check if we have more representations to process
				// this assumes that no mesage can overtake another to a particular neuron. since all messages will visit all nodes it 
				// should be impossible that a final signal arrives before another.
				if(s.flags.contains("FINAL")) {
					// then this was the last representation, eval all and tell parent we're done, reset settings
	
					val ep = e.evaluateEpoch

					log.debug("Epoch finished. Fitness is {}", ep.fitness)

					val resetT = t.copy(expectedOutputs = Map.empty, actualOutputs = Map.empty, evaluator = ep.reset)
					context.parent ! Done(ep, t.genome)
					stay using resetT
				} else {
					val newT = t.copy(actualOutputs = updateOutputs, evaluator = e)
					environment ! Perceive()
					stay using newT
				}
			} else {
				// We would evaluate if we've not received all outputs yet...
				//val e = t.evaluator.evaluateIteration(t.expectedOutputs(s.batchId), updateOutputs(s.batchId))
				val newT = t.copy(actualOutputs = updateOutputs)
				stay using newT	
			}

		// network update comes as a result of crossover
		case Event(ng: NetworkUpdate, t: NetworkSettings) =>
      		val updatedGenome = ng.f.generate
      		// do this here because crossover may introduce a neruon isn't in this schema.. possibly?

      		// this schema is not up to date... we must update the schema based on the new, crossed genome...
      		log.debug("new genome {}, to schema {}, chilren for context: {}", updatedGenome.toJson, t.networkSchema, context.children)
      		
      		val updatedSettings = t.copy(genome = updatedGenome)
			
			log.debug("New network generated: {}",updatedGenome)
      		// Here we should decide whether or not to mutate the new genome... 
      		// if we mutate - > go to status mutating
      		// if we don't mutate, go to ready and perceive

      		val mr = configData.globalMutationRate
      		if(Random.nextDouble < mr) {
      			
      			val mutationAction = mutator.mutate(updatedGenome) 
      			mutationAction match {
      				case m: Innovation.WeightChangeInnovation => {
      					val newGenome = m.genome
      					val updatedSchemaWeight = generateActors(t.networkSchema, newGenome)
      					val updatedSettingsWeight = updatedSettings.copy(genome = newGenome, networkSchema = updatedSchemaWeight)

      					// log.debug("mutating genome weights, currently has no effect")

      					environment ! Perceive()
						stay using updatedSettingsWeight
      				}

      				case _ => {
      					log.debug("mutating genome, request will be {}", mutationAction)
						innovation ! mutationAction
						goto(Mutating) using updatedSettings
      				}
      			}
      			
      		
      		} else {
      			// even thought there's no further mutation.. we may have had cross over. so we should update the genome and schema.

				val updatedSchemaNoMut = generateActors(t.networkSchema, updatedGenome)
     			val updatedSettingsNoMut = updatedSettings.copy(genome = updatedGenome, networkSchema = updatedSchemaNoMut)

      			log.debug("new network received and actors updated")
      			environment ! Perceive()
				stay using updatedSettingsNoMut	
      		}
	}

	onTransition {
		case Mutating -> NetworkActive =>
			
			environment ! Perceive()
	}

	when(Mutating) {

		case Event(s: Innovation.InnovationConfirmation, t: NetworkSettings) =>
			log.debug("received connection innovation confirmation, will update genome. innovation id: {}, from {}, to {}", s.id, s.from, s.to)

			val updatedGenome = t.genome.updateNetworkGenome(s)
			// update schema, remember this has got a net it knows nothing about.. in theory..
			val updatedSchema = generateActors(t.networkSchema, updatedGenome)
			val updatedSettings = t.copy(genome = updatedGenome, networkSchema = updatedSchema)
			//val newlyupdatedconnectionGenome = updatedGenome.connections(s.id)
			log.debug("new genome is {}", updatedGenome.toJson)
			
			// tell to neuron it has a new Predecessor, tell from neuron it has a new Successor

			//val fromActor = updatedSettings.networkSchema.allNodes(s.from)
			//val toActor = updatedSettings.networkSchema.allNodes(s.to)

			//val trecurrent = {updatedGenome.neurons(s.to).layer <= updatedGenome.neurons(s.from).layer}

			//toActor.actor ! Neuron.ConnectionConfigUpdate(inputs = List(Predecessor(fromActor, recurrent = trecurrent)))
			//fromActor.actor ! Neuron.ConnectionConfigUpdate(outputs = List(Successor(node = toActor, weight = newlyupdatedconnectionGenome.weight, recurrent = trecurrent)))

			goto(NetworkActive) using updatedSettings

		case Event(s: Innovation.SubnetConnectionInnovationConfirmation, t: NetworkSettings) =>


			val logparams =Array( s.originalRequest.existingNetId, s.updatedNetTracker, s.updatedConnectionTracker, s.originalRequest.from, s.originalRequest.to)
			log.debug("received innovation confirmation for subnet {} new id {}. conn innovation id: {}, from {}, to {}",logparams)
			// first we need to re-get the subnet part that we are supposed to be mutating. 
			// then, add the connection to the genome,
			// then create the connections and send to neuron actors. or rather to the subnetwork actor.
			
			val updatedGenome = t.genome.updateSubnet(s)
			val updatedSchema = generateActors(t.networkSchema, updatedGenome)
			val updatedSettings = t.copy(genome = updatedGenome, networkSchema = updatedSchema)

			val newlyupdatedsubnetGenome = updatedGenome.subnets.get(s.updatedNetTracker)
			val newlyupdatedconnectionGenome = newlyupdatedsubnetGenome.connections(s.updatedConnectionTracker)
			log.debug("updated the subnet and network genome now: {}, going to send to node {} which is {}", updatedGenome.toJson, s.originalRequest.neuronId, nodeSchema.allNodes(s.originalRequest.neuronId).actor)

			updatedSettings.networkSchema.allNodes(s.originalRequest.neuronId).actor ! SubNetwork.ConnectionUpdate(newlyupdatedsubnetGenome,newlyupdatedconnectionGenome)

			goto(NetworkActive) using updatedSettings


		case Event(s: Innovation.NetworkNeuronInnovationConfirmation, t: NetworkSettings) =>
			log.debug("received confirmation of new neuron {}", s)

			val updatedGenome = t.genome.updateNetworkGenome(s)
			// generate the new actor
			val updatedSchema = generateActors(t.networkSchema, updatedGenome)
			val updatedSettings = t.copy(genome = updatedGenome, networkSchema = updatedSchema)
			// need to tell two neurons that they have a disabled connection and a new one.
			log.debug("genome updated now: {}, represented as : {}", updatedGenome.toJson, updatedGenome.toJson)
			goto(NetworkActive) using updatedSettings

		case Event(s: Innovation.SubNetNeuronInnovationConfirmation, t: NetworkSettings) =>
			log.debug("received confirmation of new neuron {} for subnet", s)

			val updatedGenome = t.genome.updateSubnet(s)
			// generate the new actor
			val updatedSchema = generateActors(t.networkSchema, updatedGenome)
			val updatedSettings = t.copy(genome = updatedGenome, networkSchema = updatedSchema)
			// need to tell two neurons that they have a disabled connection and a new one.
			log.debug("genome updated now: {}, represented as : {}", updatedGenome, updatedGenome.toJson)
			goto(NetworkActive) using updatedSettings



	}

	when(NetworkTest) {

		case Event(s: Neuron.Signal, t: NetworkSettings) =>

			log.debug("received signal of {}", s.value)
			nodeSchema.in.nodes.foreach {i =>
			 	i.actor ! s
				}

			stay using t

		case Event(r: Representation, t: NetworkSettings) =>

			log.debug("received representation of {} will generate batch {}", r.input, t.rCount)
			
			// Send the signals off into the network, send signal with a batch id (rCount)
			r.input.foreach(i => {
				nodeSchema.allNodes(i._1).actor ! Neuron.Signal(i._2, r.flags, t.rCount)
			})

			val updateExpected = t.expectedOutputs + (t.rCount -> r)

			stay using t.copy(rCount = t.rCount + 1, expectedOutputs = updateExpected)

		case Event(s: Neuron.Output, t: NetworkSettings) =>

			log.debug("network received Output signal of {} from {}, reps processed: {}", s, sender(), t.expectedOutputs)

			// add signal to actual outputsReceived
			val existing = t.actualOutputs.getOrElse(s.batchId, Map.empty)
			val updateExisting = existing + (s.nodeId -> s.value)
			val updateOutputs = t.actualOutputs + (s.batchId -> updateExisting) 
			
			
			// check have we processed a representation complete?
			if(updateOutputs(s.batchId).size == t.expectedOutputs(s.batchId).expectedOutput.size) {
				
				out ! "" + t.expectedOutputs(s.batchId).input.values.mkString(",") + "," + 	t.expectedOutputs(s.batchId).expectedOutput.values.mkString(",") + ", " + updateExisting.values.mkString(",") 
				

				// now double check if we have more representations to process
				// this assumes that no mesage can overtake another to a particular neuron. since all messages will visit all nodes it 
				// should be impossible that a final signal arrives before another.
				if(s.flags.contains("FINAL")) {
					// then this was the last representation, eval all and tell parent we're done, reset settings

					val resetT = t.copy(expectedOutputs = Map.empty, actualOutputs = Map.empty)
					context.parent ! Completed
					stay using resetT
				
				} else {
				
					val newT = t.copy(actualOutputs = updateOutputs)
					environment ! Perceive()
					stay using newT
				
				}
			} else {
				
				val newT = t.copy(actualOutputs = updateOutputs)
				stay using newT	
			}

	}


    	/*
	 * Network Actor has a method generate Actors so that subnetworks and networks can
	 * generate actor networks from the class based genome rather than directly from Json.
	 * Logic is added to check the schema to see if the node already exists or not.
	 */

	 def generateActors(networkSchema: NetworkNodeSchema, networkGenome: NetworkGenome): NetworkNodeSchema = {
	 	val subnets = networkGenome.subnets
	 	val neurons = networkGenome.neurons
	 	val newNetworkSchema: NetworkNodeSchema = networkGenome.neurons.foldLeft(networkSchema){ (schemaObj, current) =>
	 		val currentObj = current._2

		 	currentObj.subnetId match {

			 	case Some(subnet) =>
				 	
				 	// already an actor for this genome?
				 	{if(!schemaObj.allNodes.contains(currentObj.id)){
				 		val subnetStructure = subnets.get(subnet)
					    val sn: ActorRef = context.actorOf(SubNetwork.props("subnet-" + currentObj.name, currentObj, subnetStructure), "subnet-" + currentObj.name)
					    schemaObj.update(currentObj, sn)
				 	} else {
				 		
				 		// get the new genome structure for the subnet
				 		val subnetStructure = subnets.get(subnet)
				 		// find the actor 
				 		val sn: ActorRef = schemaObj.allNodes(currentObj.id).actor
				 		// send the genome srtucture to the subnet
				 		sn ! subnetStructure

				 		schemaObj
				 	}}
				 	
			 case None =>
			 		// already an actor for this genome?
				 	{if(!schemaObj.allNodes.contains(currentObj.id)){
				 		// there is no actor, so create one
				 		val ar: ActorRef =  if(currentObj.layer > 1) context.actorOf(Neuron.props(currentObj), currentObj.name) else self
				 		schemaObj.update(currentObj, ar)
		 	 		} else {
		 	 			//send the structure to the neuron to update things such as bias
		 	 			// but no need to if this is an input
		 	 			if(currentObj.layer > 1) {
		 	 				val sn: ActorRef = schemaObj.allNodes(currentObj.id).actor
		 	 				sn ! currentObj
		 	 			}
				 		schemaObj
				 	}}
	 		}
	 	}

	 	// now close down any neurons not used.

	 	context.children.foreach(c=> if(!newNetworkSchema.allNodes.values.exists(x	=> x.actor.path.name == c.path.name)) context.stop(c))

	 	// Now send down the connections to remaining neurons

	 	val (connectionConfigs, inputnodes) = networkGenome.connections.foldLeft((Map[ActorRef, Neuron.ConnectionConfig](), Map[Int, List[ActorRef]]())) { (acc, current) =>
	 		
	 		val (connectionId, connection) = current
	 		val (actorConnectionConfigs, inputConnectedActors) = acc

  	 		if (connection.enabled) {
	 			// If the connection is enabled update the config obj. 

	 			val fromActor = newNetworkSchema.allNodes(connection.from)
				val toActor = newNetworkSchema.allNodes(connection.to)


		 		val pre = Predecessor(fromActor, connection.recurrent)
		 		val suc = Successor(toActor, connection.weight, connection.recurrent)


		 		val updatedInputConnectedActors: Map[Int, List[ActorRef]] = if(connection.isConnectedInput){
		 				inputConnectedActors.get(connection.from) match {
		 					case Some(existing: List[ActorRef]) => 
		 						val newActorList: List[ActorRef] = toActor.actor :: existing
		 						inputConnectedActors + (connection.from -> newActorList)
		 					case None =>
		 						
		 						inputConnectedActors + (connection.from -> List(toActor.actor)) 
		 				}
		 				
		 			} else {
		 				inputConnectedActors
		 			}

		 		// Create configs for all inputs
		 		val updateIncoming: Map[ActorRef, Neuron.ConnectionConfig] = actorConnectionConfigs get toActor.actor match {
			 		
			 		case Some(existingConfig) => {
				 	

			 			// we already have at least one connection for this actor so update the config by adding a new incomin connection to it.

				 		actorConnectionConfigs + (toActor.actor -> existingConfig.copy(inputs = pre :: existingConfig.inputs))
				 	
				 	}
			 		
			 		case None => 

			 			// this is the first connection we have so create a new config.

			 			actorConnectionConfigs + (toActor.actor -> Neuron.ConnectionConfig(inputs = List(pre), neuronGenome = neurons(connection.to)))
		 		}

		 		// create config for all outputs
		 		updateIncoming get newNetworkSchema.allNodes(connection.from).actor match {
			 		case Some(existingConfig) => {

			 			// we already have at least one connection for this actor so update the config by adding a new outgoing connection to it.

				 		(updateIncoming + (fromActor.actor ->  existingConfig.copy(outputs = suc :: existingConfig.outputs)), updatedInputConnectedActors)
				 	}
			 		case None => 

			 				// this is the first connection we have so create a new config.

			 			(updateIncoming + (fromActor.actor -> Neuron.ConnectionConfig(outputs = List(suc), neuronGenome = neurons(connection.from))), updatedInputConnectedActors)
			 	}
	 	} else {
			(actorConnectionConfigs, inputConnectedActors) 
		}
	}

		// Maps of actor and their connection config has been created.
		// It's now possible to send the configs to the actors.

		connectionConfigs.foreach(c => {
			//first send config
			c._1 ! c._2
	 	})


		newNetworkSchema.copy(inNodes = inputnodes)
   	}




}
