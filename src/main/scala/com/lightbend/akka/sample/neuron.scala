package com.thingy.neuron

import akka.actor.{ ActorRef, FSM }
import com.thingy.activationfunctions.ActivationFunction

// States of Neuron
sealed trait NeuronState
case object Initialising extends NeuronState
case object Ready extends NeuronState
case object Active extends NeuronState


// State Data holder
final case class NeuronSettings(
	activationFunction: ActivationFunction = ActivationFunction("SIGMOID"),
	activationLevel: Double = 0,
	signalsReceived: Int = 0,
	connections: Neuron.ConnectionConfig = Neuron.ConnectionConfig())

object Neuron {

	//
	trait InputConnections
	trait OutputConnections

	// Messages it can receive
	final case class Signal(value: Double)
	final case class ConnectionConfig(inputs: List[Predecessor] = List.empty, outputs: List[Successor] = List.empty)
	final case class ConnectionConfigUpdate(inputs: List[Predecessor] = List.empty, outputs: List[Successor] = List.empty)
	

}

class Neuron extends FSM[NeuronState, NeuronSettings] {

	import Neuron._

	startWith(Initialising, NeuronSettings())

	when(Initialising) {
		case Event(d: ConnectionConfig, t: NeuronSettings) =>

			log.debug("received settings config update of {} inputs, and {} outputs", d.inputs.length, d.outputs.length)
			val updatedConfig = t.connections.copy(inputs = d.inputs ++ t.connections.inputs, outputs = d.outputs ++ t.connections.outputs)
			goto(Ready) using t.copy(connections = updatedConfig)

	}



	when(Ready) {

		case Event(d: ConnectionConfig, t: NeuronSettings) =>

			log.debug("received settings config of {} inputs, and {} outputs", d.inputs.length, d.outputs.length)
			// Overwrite all existing connection details.
			val updatedConfig = t.connections.copy(inputs = d.inputs, outputs = d.outputs)
			stay using t.copy(connections = updatedConfig)

		case Event(d: ConnectionConfigUpdate, t: NeuronSettings) =>

			log.debug("received settings config update of {} inputs, and {} outputs", d.inputs.length, d.outputs.length)
			// Append to list of connections
			val updatedConfig = t.connections.copy(inputs = d.inputs ++ t.connections.inputs, outputs = d.outputs ++ t.connections.outputs)
			stay using t.copy(connections = updatedConfig)


    	case Event(s: Signal, t: NeuronSettings) =>

			val newT = t.copy(signalsReceived = t.signalsReceived + 1, activationLevel = t.activationLevel + s.value )

			log.debug("{} neuron got signal,  now {}", context.self, newT.activationLevel)

			if (newT.signalsReceived == newT.connections.inputs.length || newT.connections.inputs.length == 0) {

				t.connections.outputs.foreach(output => output.node.actor ! Signal(s.value * output.weight.value))

				stay using newT

			}
			else {

				stay using newT

			}
  	}

	initialize()

}
