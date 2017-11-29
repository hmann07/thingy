package com.thingy.network

import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.genome._
import com.thingy.neuron.Neuron
import play.api.libs.json._
import com.thingy.neuron.{Successor, Predecessor}
import com.thingy.weight.Weight

sealed trait NetworkState
case object Initialising extends NetworkState
case object Ready extends NetworkState
case object Active extends NetworkState


// State Data holder
final case class NetworkSettings(id: Int = 1)

object Network {

	// Messages it can receive
	final case class Signal(value: Double)

    // an override of props to allow Actor to take constructor args.
	// Network should take a genome and create a number of sub networks.

	def props(name: String, networkGenome: NetworkGenomeBuilder): Props = {

		Props(classOf[Network], name, networkGenome)
	}
}

class Network(name: String, networkGenome: NetworkGenomeBuilder) extends FSM[NetworkState, NetworkSettings] {

	import Network._
	log.debug("network: {} created", name)

	// First create input neurons. Select these on the basis that they belong to layer 0.
	// Input neurons will not have any internal node structures so can be created as straight Neurons.
	// By giving the context the neurons will be created in this context.

	val genome = networkGenome.generateFromSeed
	val generatedActors = genome.generateActors(context)

	log.debug("generated genome: {}, actors are setup as {} ", genome, generatedActors)


	startWith(Ready, NetworkSettings())

	when(Ready) {
		case Event(s: Neuron.Signal, t: NetworkSettings) =>

			log.debug("received signal of {}", s.value)

			generatedActors.in.nodes.foreach {i =>
			 	i.actor ! s
				}

			goto(Ready) using t

	}

}
