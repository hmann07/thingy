 package com.thingy.neuron

import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.activationfunctions.ActivationFunction
import com.thingy.genome.NeuronGenome

// States of Neuron
sealed trait NeuronState
case object Initialising extends NeuronState
case object Ready extends NeuronState
case object Active extends NeuronState


// State Data holder
final case class NeuronSettings(
	firstRun: Boolean = true,
	recurrentSignalsReceived: Int = 0,
	activationFunction: ActivationFunction = ActivationFunction("SIGMOID"),
	activationLevel: Double = 0,
	recurrentSignal: Double = 0.0,
	signalsReceived: Int = 0,
	connections: Neuron.ConnectionConfig = Neuron.ConnectionConfig(),
	genome: NeuronGenome) {
	def reset {
		this.copy()
	}
}

object Neuron {

	//
	trait InputConnections
	trait OutputConnections

	// Messages it can receive
	final case class Signal(value: Double, flags:List[String] = List.empty, batchId: Int = 0, recurrent: Boolean = false)
	final case class Output(nodeId: Int, value: Double, batchId: Int = 0,  flags: List[String] = List.empty)
	final case class ConnectionConfig(inputs: List[Predecessor] = List.empty, outputs: List[Successor] = List.empty, neuronGenome: NeuronGenome= null)
	final case class ConnectionConfigUpdate(inputs: List[Predecessor] = List.empty, outputs: List[Successor] = List.empty)
	
	def props(genome: NeuronGenome): Props = {
		Props(classOf[Neuron], genome)
	}	

}

class Neuron(genome: NeuronGenome) extends FSM[NeuronState, NeuronSettings] {

	import Neuron._

	startWith(Initialising, NeuronSettings(genome = genome))

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
			
			val updatedSettings = t.copy(
				connections = updatedConfig,
				firstRun = true,
				recurrentSignalsReceived = 0,
				activationLevel = 0,
				recurrentSignal = 0.0,
				signalsReceived = 0,
				genome = d.neuronGenome
			)
			// assumethis is a new epoch, clear recurrent received.. (won't have fired on last pattern so won't have reset counts)
			stay using updatedSettings

		case Event(d: ConnectionConfigUpdate, t: NeuronSettings) =>

			log.debug("received settings config update of {} inputs, and {} outputs", d.inputs.length, d.outputs.length)
			// Append to list of connections
			val updatedConfig = t.connections.copy(inputs = d.inputs ++ t.connections.inputs, outputs = d.outputs ++ t.connections.outputs)
			
			val updatedSettings = t.copy(
				connections = updatedConfig,
				firstRun = true,
				recurrentSignalsReceived = 0,
				activationLevel = 0,
				recurrentSignal = 0.0,
				signalsReceived = 0
			)
			// assumethis is a new epoch, clear recurrent received.. (won't have fired on last pattern so won't have reset counts)
			stay using updatedSettings

    	case Event(s: Signal, t: NeuronSettings) =>

    		if(t.firstRun){
    			// then no need to wait for rec before firing
    			// but we should accumulate the signal
    			if(s.recurrent) {
    				val newT = t.copy(
    					recurrentSignal = t.recurrentSignal + s.value, 
    					recurrentSignalsReceived = t.recurrentSignalsReceived + 1
    					)

    				val o = Array(t.genome, s, newT.activationLevel, newT.signalsReceived, newT.connections.inputs.length)
					log.debug("{} neuron on first run got recurrent signal {},  now {}, received {} out of {} ", o)
    				stay using newT
    			} else {
    				val newT = t.copy(
    					signalsReceived = t.signalsReceived + 1, 
    					activationLevel = t.activationLevel + s.value )

    				val o = Array(t.genome, s, newT.activationLevel, newT.signalsReceived, newT.connections.inputs.length)
					log.debug("{} neuron on first run got normal signal {},  now {}, received {} out of {} ", o)

					// check if we've had all normal signals
					if (newT.signalsReceived == newT.connections.inputs.filter(i=> !i.recurrent).length || newT.connections.inputs.length == 0) {
						// reset the neuron
						val resetT = t.copy(signalsReceived = 0, activationLevel = 0, firstRun = false)
						//log.debug("Neuron Firing")
						// Decide what to do next
						t.genome.nodeType match {
							case "input" => t.connections.outputs.foreach(output => output.node.actor ! s.copy(value = newT.activationLevel * output.weight.value))
							case "output" => {
								context.parent ! Output(t.genome.id, t.activationFunction.function(newT.activationLevel + (t.genome.biasWeight.value * -1))  , s.batchId, s.flags)
								t.connections.outputs.foreach(output => output.node.actor ! s.copy(value = t.activationFunction.function(newT.activationLevel + (t.genome.biasWeight.value * -1)) * output.weight.value, recurrent = output.recurrent))
							}
							case "hidden" => {
								t.connections.outputs.foreach(output => output.node.actor ! s.copy(value = t.activationFunction.function(newT.activationLevel + (t.genome.biasWeight.value * -1)) * output.weight.value, recurrent = output.recurrent))
							}
						}
						// reset the neuron
						stay using resetT
    				} else {
    					// not had all signals, keep waiting
    					stay using newT
    				}
    			}
    		} else {
    			// this isn't the first run.
    			// do more or less the same thing... bu tthe check for completion is different

    			if(s.recurrent) {

    				val newT = t.copy(
    					recurrentSignal = t.recurrentSignal + s.value, 
    					recurrentSignalsReceived = t.recurrentSignalsReceived + 1
    					)
    				val o = Array(t.genome, s, newT.activationLevel, newT.signalsReceived, newT.connections.inputs.length)
    				log.debug("{} neuron on non-first run got recurrent signal {},  now {}, received {} out of {} ", o)
    				// this could in fact be the last signal received, despite being the first sent..
    				// so we should check and handle
    				if (((newT.signalsReceived + newT.recurrentSignalsReceived) == newT.connections.inputs.length) || newT.connections.inputs.length == 0) {
						// reset the neuron
						val resetT = t.copy(signalsReceived = 0, activationLevel = 0.0, recurrentSignalsReceived = 0, recurrentSignal = 0.0)
						//log.debug("Neuron Firing")
						// Decide what to do next
						t.genome.nodeType match {
							case "input" => t.connections.outputs.foreach(output => output.node.actor ! s.copy(value = newT.activationLevel * output.weight.value))
							case "output" => {
								context.parent ! Output(t.genome.id, t.activationFunction.function(newT.activationLevel + newT.recurrentSignal + (t.genome.biasWeight.value * -1))  , s.batchId, s.flags)
								t.connections.outputs.foreach(output => output.node.actor ! s.copy(value = t.activationFunction.function(newT.activationLevel + newT.recurrentSignal + (t.genome.biasWeight.value * -1)) * output.weight.value, recurrent = output.recurrent))
							}
							case "hidden" => {
								t.connections.outputs.foreach(output => output.node.actor ! s.copy(value = t.activationFunction.function(newT.activationLevel + newT.recurrentSignal +  (t.genome.biasWeight.value * -1)) * output.weight.value, recurrent = output.recurrent))
							}
						}
						// reset the neuron
						stay using resetT
					} else {
						// not had all signals, keep waiting
						stay using newT
					}

    			} else {
    				
    				// not a recurrentSignal
    				val newT = t.copy(
    					signalsReceived = t.signalsReceived + 1, 
    					activationLevel = t.activationLevel + s.value )

    				val o = Array(t.genome, s, newT.activationLevel, newT.signalsReceived, newT.connections.inputs.length)
					log.debug("{} neuron on non-first run got signal {},  now {}, received {} out of {} ", o)

					// check if we've had all normal signals
					if (((newT.signalsReceived + newT.recurrentSignalsReceived) == newT.connections.inputs.length) || newT.connections.inputs.length == 0) {
						// reset the neuron
						val resetT = t.copy(signalsReceived = 0, activationLevel = 0.0, recurrentSignalsReceived = 0, recurrentSignal = 0.0)
						//log.debug("neruon firing")
						// Decide what to do next
						t.genome.nodeType match {
							case "input" => t.connections.outputs.foreach(output => output.node.actor ! s.copy(value = newT.activationLevel * output.weight.value))
							case "output" => {
								context.parent ! Output(t.genome.id, t.activationFunction.function(newT.activationLevel + newT.recurrentSignal + (t.genome.biasWeight.value * -1))  , s.batchId, s.flags)
								t.connections.outputs.foreach(output => output.node.actor ! s.copy(value = t.activationFunction.function(newT.activationLevel + newT.recurrentSignal + (t.genome.biasWeight.value * -1)) * output.weight.value, recurrent = output.recurrent))
							}
							case "hidden" => {
								t.connections.outputs.foreach(output => output.node.actor ! s.copy(value = t.activationFunction.function(newT.activationLevel + newT.recurrentSignal +  (t.genome.biasWeight.value * -1)) * output.weight.value, recurrent = output.recurrent))
							}
						}
						// reset the neuron
						stay using resetT
					} else {
					// not had all signals, keep waiting
					stay using newT
					} 
				}
			}
  	}

	initialize()

}
