package com.thingy.subnetwork

import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.neuron.{Successor, Predecessor, Neuron}
import com.thingy.genome.NetworkGenome


sealed trait SubNetworkState
case object Initialising extends SubNetworkState
case object Ready extends SubNetworkState
case object Active extends SubNetworkState

// State Data holder

final case class SubNetworkSettings(
	activationLevel: Double = 0,
	signalsReceived: Int = 0,
	connections: Neuron.ConnectionConfig = Neuron.ConnectionConfig(),
	genome: NetworkGenome.NetworkGenome,
	networkSchema: NetworkGenome.NetworkNodeSchema)



object SubNetwork {

	// Messages it can receive
		// Imported from NEURON TODO. split connections and signal into packages.
	case class ConnectionUpdate(newGenome: NetworkGenome.NetworkGenome, newConnection: NetworkGenome.ConnectionGenome)
    // an override of props to allow Actor to take constructor args
	def props(name: String, subnetGenome: NetworkGenome.NetworkGenome): Props = {

		Props(classOf[SubNetwork], name, subnetGenome)
	}
}

class SubNetwork(name: String, subnetGenome: NetworkGenome.NetworkGenome) extends FSM[SubNetworkState, SubNetworkSettings] {

	import SubNetwork._
	log.debug("sub-network: {} created", name)

	val generatedActors: NetworkGenome.NetworkNodeSchema = subnetGenome.generateActors(context, NetworkGenome.NetworkNodeSchema())

	log.debug("generated subnet genome: {}, actors are setup as {} ", subnetGenome, generatedActors)

	startWith(Initialising, SubNetworkSettings(genome = subnetGenome, networkSchema = generatedActors ))

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
			toActor.actor ! Neuron.ConnectionConfigUpdate(inputs = List(Predecessor(fromActor)))
			fromActor.actor ! Neuron.ConnectionConfigUpdate(outputs = List(Successor(toActor)))

			stay using t.copy(genome = cu.newGenome)

		case Event(g: NetworkGenome.NetworkGenome, t: SubNetworkSettings) =>
			log.debug("subnet received new NetworkGenome, now need to inform neurons and connections of new status ")
			val updatedSchema = g.generateActors(context, t.networkSchema)
			log.debug("subnets updated schema {}", updatedSchema)
			stay using t.copy(genome = g, networkSchema = updatedSchema)


		case Event(d: Neuron.ConnectionConfig, t: SubNetworkSettings) =>

			log.debug("received settings config update of {} inputs, and {} outputs", d.inputs.length, d.outputs.length)
			val updatedConfig = t.connections.copy(inputs = d.inputs ++ t.connections.inputs, outputs = d.outputs ++ t.connections.outputs)
			stay using t.copy(connections = updatedConfig)


		/*
		 * If a new signal is received by the subnetwork it will need to act as the linear combiner.
		 * Once all signals are received it will then pass the combined value to a token input which will pass to relanvent connected nodes.
		 */

    	case Event(s: Neuron.Signal, t: SubNetworkSettings) =>

			val newT = t.copy(signalsReceived = t.signalsReceived + 1, activationLevel = t.activationLevel + s.value )

			log.debug("{} subnet got signal,  now {}", name, newT.activationLevel)

			if (newT.signalsReceived == newT.connections.inputs.length) {
				generatedActors.in.nodes.foreach {i => i.actor ! s}
				stay using newT
			}
			else {
				stay using newT
			}
  	}

	initialize()

}
