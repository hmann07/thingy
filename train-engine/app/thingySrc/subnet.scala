package com.thingy.subnetwork

import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.neuron.{Successor, Predecessor, Neuron}
import com.thingy.genome._
import com.thingy.network._
import com.thingy.weight.Weight


sealed trait SubNetworkState
case object Initialising extends SubNetworkState
case object Ready extends SubNetworkState
case object Active extends SubNetworkState

// State Data holder

final case class SubNetworkSettings(
	activationLevel: Double = 0,
	signalsReceived: Int = 0,
	connections: Neuron.ConnectionConfig = Neuron.ConnectionConfig(),
	nodeGenome:  NeuronGenome,
	genome: NetworkGenome,
	networkSchema: NetworkNodeSchema)



object SubNetwork {

	// Messages it can receive
		// Imported from NEURON TODO. split connections and signal into packages.
	case class ConnectionUpdate(newGenome: NetworkGenome, newConnection: ConnectionGenome)
    // an override of props to allow Actor to take constructor args
	def props(name: String, nodeGenome: NeuronGenome, subnetGenome: NetworkGenome): Props = {

		Props(classOf[SubNetwork], name, nodeGenome, subnetGenome)
	}
}

class SubNetwork(name: String, nodeGenome: NeuronGenome, subnetGenome: NetworkGenome) extends FSM[SubNetworkState, SubNetworkSettings] {

	import SubNetwork._
	import Network._

	log.debug("sub-network: {} created", name)

	val generatedActors: NetworkNodeSchema = generateActors(NetworkNodeSchema(), subnetGenome)

	log.debug("generated subnet genome: {}, actors are setup as {} ", subnetGenome, generatedActors)

	startWith(Initialising, SubNetworkSettings(nodeGenome = nodeGenome, genome = subnetGenome, networkSchema = generatedActors ))

	when(Initialising) {

		case Event(d: Neuron.ConnectionConfig, t: SubNetworkSettings) =>

			log.debug("received settings config object of {} inputs, and {} outputs", d.inputs.length, d.outputs.length)
			goto(Ready) using t.copy(connections = d)

	}

	when(Ready) {

		case Event(cu: ConnectionUpdate, t:SubNetworkSettings) =>
			
			log.debug("received new connection update")

			// first create new Connection config and send to 
			val fromActor = generatedActors.allNodes(cu.newConnection.from)
			val toActor = generatedActors.allNodes(cu.newConnection.to)

			//val recurrent = {updatedGenome.neurons(s.from).layer < updatedGenome.neurons(s.to).layer}

			toActor.actor ! Neuron.ConnectionConfigUpdate(inputs = List(Predecessor(node = fromActor, recurrent = cu.newConnection.recurrent)))
			fromActor.actor ! Neuron.ConnectionConfigUpdate(outputs = List(Successor(node = toActor, weight = cu.newConnection.weight, recurrent = cu.newConnection.recurrent)))

			stay using t.copy(genome = cu.newGenome)

		case Event(g: NetworkGenome, t: SubNetworkSettings) =>
			log.debug("subnet received new NetworkGenome, now need to inform neurons and connections of new status ")
			val updatedSchema = generateActors(t.networkSchema, g)
			log.debug("subnets updated schema {}", updatedSchema)
			stay using t.copy(genome = g, networkSchema = updatedSchema)


		case Event(d: Neuron.ConnectionConfig, t: SubNetworkSettings) =>

			log.debug("received settings config of {} inputs, and {} outputs", d.inputs.length, d.outputs.length)
			// Overwrite all existing connection details.
			val updatedConfig = t.connections.copy(inputs = d.inputs, outputs = d.outputs)
			stay using t.copy(connections = updatedConfig)

		case Event(d: Neuron.ConnectionConfigUpdate, t: SubNetworkSettings) =>

			log.debug("received settings config update of {} inputs, and {} outputs", d.inputs.length, d.outputs.length)
			// Append to list of connections
			val updatedConfig = t.connections.copy(inputs = d.inputs ++ t.connections.inputs, outputs = d.outputs ++ t.connections.outputs)
			stay using t.copy(connections = updatedConfig)


		/*
		 * If a new signal is received by the subnetwork it will need to act as the linear combiner.
		 * Once all signals are received it will then pass the combined value to a token input which will pass to relanvent connected nodes.
		 */

    	case Event(s: Neuron.Signal, t: SubNetworkSettings) =>


    		if(s.recurrent) {

    			stay

    		} else {
				val newT = t.copy(signalsReceived = t.signalsReceived + 1, activationLevel = t.activationLevel + s.value )

				val o = Array(s, newT.activationLevel, newT.signalsReceived, newT.connections.inputs.length)
				log.debug("Subnet got signal {},  now {}, received {} out of {} ", o)


				if (newT.signalsReceived == newT.connections.inputs.filter(!_.recurrent).length) {
					val resetT = t.copy(signalsReceived = 0, activationLevel = 0)

					generatedActors.in.nodes.foreach {i => i.actor ! s.copy(value = newT.activationLevel)}
					stay using resetT
				}
				else {
					stay using newT
				}
			}


		case Event(s: Neuron.Output, t: SubNetworkSettings) =>

			log.debug("subnet received Output signal of {}", s)

			if(t.nodeGenome.nodeType != "output") {
					t.connections.outputs.foreach(output => output.node.actor ! Neuron.Signal(value = s.value * output.weight.value, recurrent = output.recurrent))
				} else {
					// layer = 1, Assume layer > 1 is impossible. send to parent. 
					log.debug("output neuron sending output")
					context.parent ! s.copy(nodeId = t.nodeGenome.id)
					// and any recurrent
					t.connections.outputs.foreach(output => output.node.actor ! Neuron.Signal(value = s.value * output.weight.value, recurrent = output.recurrent))
				}
			
			val resetT = t.copy(signalsReceived = 0, activationLevel = 0)	
			stay using resetT

  	}


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

				 		val ar: ActorRef =  context.actorOf(Neuron.props(currentObj), currentObj.name )
				 		schemaObj.update(currentObj, ar)
		 	 		} else {
		 	 			//send the structure to the neuron to update things such as bias
		 	 			val sn: ActorRef = schemaObj.allNodes(currentObj.id).actor
		 	 			sn ! currentObj
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

				 		(updateIncoming + (fromActor.actor ->  existingConfig.copy(outputs = suc :: existingConfig.outputs)), inputConnectedActors)
				 	}
			 		case None => 

			 				// this is the first connection we have so create a new config.

			 			(updateIncoming + (fromActor.actor -> Neuron.ConnectionConfig(outputs = List(suc), neuronGenome = neurons(connection.from))), inputConnectedActors)
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

	initialize()

}
