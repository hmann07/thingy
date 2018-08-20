package com.thingy.network

import com.thingy.config.ConfigDataClass.ConfigData
import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.genome._
import com.thingy.neuron.Neuron
import play.api.libs.json._
import com.thingy.neuron.{Successor, Predecessor}
import com.thingy.weight.Weight
import com.thingy.innovation._
import com.thingy.subnetwork.SubNetwork
import com.thingy.mutator.Mutator
import com.thingy.environment.Environment.Representation
import com.thingy.evaluator._
import scala.util.Random
import com.typesafe.config.ConfigFactory

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
		networkSchema: NetworkGenome.NetworkNodeSchema,
		rCount: Int = 0,
		expectedOutputs: Map[Int, Representation] = Map.empty, // Map[Batchid, Representation]
		actualOutputs: Map[Int, Map[Int, Double]] = Map.empty, // Map[Batchid, Map[OuputputId, ActualOutputValue]]
		evaluator: Evaluator = XOREvaluator()
		 )

object Network {

	implicit val config = ConfigFactory.load()
	

	// Messages it can receive
	final case class Signal(value: Double)
	final case class Mutate()
	final case class Perceive()
	case class Mutated()
	case class NetworkUpdate(f: GenomeIO)
	case class Done(evaluator: Evaluator, genome: NetworkGenome)
	case class Completed()
    // an override of props to allow Actor to take con structor args.
	// Network should take a genome and create a number of sub networks.

	def props(name: String, networkGenome: NetworkGenome, innovation: ActorRef, environment: ActorRef, configData: ConfigData, startState: NetworkState, out: ActorRef = null): Props = {

		Props(classOf[Network], name, networkGenome, innovation, environment, configData, startState, out)
	}
}

class Network(name: String, ng: NetworkGenome, innovation: ActorRef, environment: ActorRef, configData: ConfigData, startState: NetworkState, out: ActorRef = null) extends FSM[NetworkState, NetworkSettings] {

	import Network._
	val networkGenome = ng

	log.debug("network: {} created with genome {}", name, networkGenome)

	// First create input neurons. Select these on the basis that they belong to layer 0.
	// Input neurons will not have any internal node structures so can be created as straight Neurons.
	// By giving the context the neurons will be created in this context.
	
	val generatedActors: NetworkGenome.NetworkNodeSchema  = networkGenome.generateActors(context, NetworkGenome.NetworkNodeSchema())
	val mutator = new Mutator(configData)

	log.debug("Network actors are setup as {} ", generatedActors)

	environment ! Perceive()

	startWith(startState, NetworkSettings(genome = networkGenome, networkSchema = generatedActors))

	when(NetworkActive) {

		case Event(s: Network.Mutate, t: NetworkSettings) =>
			
			log.debug("mutating genome")
			
			innovation ! mutator.mutate(t.genome) 
			
			goto(Mutating) using t

		case Event(s: Neuron.Signal, t: NetworkSettings) =>

			log.debug("received signal of {}", s.value)
			generatedActors.in.nodes.foreach {i =>
			 	i.actor ! s
				}

			stay using t

		case Event(r: Representation, t: NetworkSettings) =>

			log.debug("received representation of {} will generate batch {}", r.input, t.rCount)
			
			// Send the signals off into the network, send signal with a batch id (rCount)
			r.input.foreach(i => {
				generatedActors.allNodes(i._1).actor ! Neuron.Signal(i._2, r.flags, t.rCount)
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
				val e = t.evaluator.evaluateIteration(t.expectedOutputs(s.batchId), updateOutputs(s.batchId))
				val newT = t.copy(actualOutputs = updateOutputs, evaluator = e)
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
      					val updatedSchemaWeight = newGenome.generateActors(context, t.networkSchema)
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
      			
				val updatedSchemaNoMut = updatedGenome.generateActors(context, t.networkSchema)
     			val updatedSettingsNoMut = updatedSettings.copy(networkSchema = updatedSchemaNoMut)

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
			val updatedSchema = updatedGenome.generateActors(context,t.networkSchema)
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
			val updatedSchema = updatedGenome.generateActors(context,t.networkSchema)
			val updatedSettings = t.copy(genome = updatedGenome, networkSchema = updatedSchema)

			val newlyupdatedsubnetGenome = updatedGenome.subnets.get(s.updatedNetTracker)
			val newlyupdatedconnectionGenome = newlyupdatedsubnetGenome.connections(s.updatedConnectionTracker)
			log.debug("updated the subnet and network genome now: {}, going to send to node {} which is {}", updatedGenome.toJson, s.originalRequest.neuronId, generatedActors.allNodes(s.originalRequest.neuronId).actor)

			updatedSettings.networkSchema.allNodes(s.originalRequest.neuronId).actor ! SubNetwork.ConnectionUpdate(newlyupdatedsubnetGenome,newlyupdatedconnectionGenome)

			goto(NetworkActive) using updatedSettings


		case Event(s: Innovation.NetworkNeuronInnovationConfirmation, t: NetworkSettings) =>
			log.debug("received confirmation of new neuron {}", s)

			val updatedGenome = t.genome.updateNetworkGenome(s)
			// generate the new actor
			val updatedSchema = updatedGenome.generateActors(context, t.networkSchema)
			val updatedSettings = t.copy(genome = updatedGenome, networkSchema = updatedSchema)
			// need to tell two neurons that they have a disabled connection and a new one.
			log.debug("genome updated now: {}, represented as : {}", updatedGenome.toJson, updatedGenome.toJson)
			goto(NetworkActive) using updatedSettings

		case Event(s: Innovation.SubNetNeuronInnovationConfirmation, t: NetworkSettings) =>
			log.debug("received confirmation of new neuron {} for subnet", s)

			val updatedGenome = t.genome.updateSubnet(s)
			// generate the new actor
			val updatedSchema = updatedGenome.generateActors(context, t.networkSchema)
			val updatedSettings = t.copy(genome = updatedGenome, networkSchema = updatedSchema)
			// need to tell two neurons that they have a disabled connection and a new one.
			log.debug("genome updated now: {}, represented as : {}", updatedGenome, updatedGenome.toJson)
			goto(NetworkActive) using updatedSettings



	}

	when(NetworkTest) {

		case Event(s: Neuron.Signal, t: NetworkSettings) =>

			log.debug("received signal of {}", s.value)
			generatedActors.in.nodes.foreach {i =>
			 	i.actor ! s
				}

			stay using t

		case Event(r: Representation, t: NetworkSettings) =>

			log.debug("received representation of {} will generate batch {}", r.input, t.rCount)
			
			// Send the signals off into the network, send signal with a batch id (rCount)
			r.input.foreach(i => {
				generatedActors.allNodes(i._1).actor ! Neuron.Signal(i._2, r.flags, t.rCount)
			})

			val updateExpected = t.expectedOutputs + (t.rCount -> r)

			stay using t.copy(rCount = t.rCount + 1, expectedOutputs = updateExpected)

		case Event(s: Neuron.Output, t: NetworkSettings) =>

			log.debug("network received Output signal of {} from {}, reps processed: {}", s, sender(), t.expectedOutputs)

			// add signal to actual outputsReceived
			val existing = t.actualOutputs.getOrElse(s.batchId, Map.empty)
			val updateExisting = existing + (s.nodeId -> s.value)
			val updateOutputs = t.actualOutputs + (s.batchId -> updateExisting) 
			
			out ! "" + s				
			
			// check have we processed a representation complete?
			if(updateOutputs(s.batchId).size == t.expectedOutputs(s.batchId).expectedOutput.size) {
				

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

}
