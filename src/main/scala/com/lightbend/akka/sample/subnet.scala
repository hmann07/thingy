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
	connections: Neuron.ConnectionConfig = Neuron.ConnectionConfig())



object SubNetwork {

	// Messages it can receive
		// Imported from NEURON TODO. split connections and signal into packages.

    // an override of props to allow Actor to take constructor args
	def props(name: String, subnetGenome: NetworkGenome.NetworkGenome): Props = {

		Props(classOf[SubNetwork], name, subnetGenome)
	}
}

class SubNetwork(name: String, subnetGenome: NetworkGenome.NetworkGenome) extends FSM[SubNetworkState, SubNetworkSettings] {

	import SubNetwork._
	log.debug("sub-network: {} created", name)

	val generatedActors = subnetGenome.generateActors(context)

	log.debug("generated subnet genome: {}, actors are setup as {} ", subnetGenome, generatedActors)

	startWith(Initialising, SubNetworkSettings())

	when(Initialising) {

		case Event(d: Neuron.ConnectionConfig, t: SubNetworkSettings) =>

				log.debug("received settings config object of {} inputs, and {} outputs", d.inputs.length, d.outputs.length)
				goto(Ready) using t.copy(connections = d)

	}



	when(Ready) {

		
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
