package com.thingy.network

import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.genome._
import com.thingy.neuron.Neuron
import play.api.libs.json._
import com.thingy.neuron.{Successor, Predecessor}
import com.thingy.weight.Weight
import com.thingy.innovation._
import com.thingy.subnetwork.SubNetwork
import com.thingy.mutator.Mutator

sealed trait NetworkState
case object Initialising extends NetworkState
case object Ready extends NetworkState
case object Active extends NetworkState
case object Mutating extends NetworkState

// State Data holder
final case class NetworkSettings(id: Int = 1, genome:  NetworkGenome.NetworkGenome)

object Network {

	// Messages it can receive
	final case class Signal(value: Double)
	final case class Mutate()
    // an override of props to allow Actor to take con structor args.
	// Network should take a genome and create a number of sub networks.

	def props(name: String, networkGenome: NetworkGenome.NetworkGenome, innovation: ActorRef): Props = {

		Props(classOf[Network], name, networkGenome, innovation)
	}
}

class Network(name: String, networkGenome: NetworkGenome.NetworkGenome, innovation: ActorRef) extends FSM[NetworkState, NetworkSettings] {

	import Network._
	log.debug("network: {} created", name)

	// First create input neurons. Select these on the basis that they belong to layer 0.
	// Input neurons will not have any internal node structures so can be created as straight Neurons.
	// By giving the context the neurons will be created in this context.
	
	val generatedActors = networkGenome.generateActors(context)
	val mutator = new Mutator

	log.debug("Network ctors are setup as {} ", generatedActors)

	startWith(Ready, NetworkSettings(genome = networkGenome))

	when(Ready) {

		case Event(s: Network.Mutate, t: NetworkSettings) =>
			
			log.debug("mutating genome")
			
			innovation ! mutator.mutate(t.genome) 
			
			//innovation ! Innovation.NetworkConnectionInnovation(3,3)
			
			//val neuronId = 3
			//val gs = t.genome.subnets.get(0) // get the first subnet genome. and simulate a new conection. his should be reset to get the subnet related to the neuron
			// if we are updatying a subnet, then we may need to update the id and the connection/neuron list.
			// so at a later point we need to identify which network in the genome needs to be updated. using the id.

			

			goto(Mutating) using t

		case Event(s: Neuron.Signal, t: NetworkSettings) =>

			log.debug("received signal of {}", s.value)
			generatedActors.in.nodes.foreach {i =>
			 	i.actor ! s
				}

			stay using t


	}

	when(Mutating) {

		case Event(s: Innovation.InnovationConfirmation, t: NetworkSettings) =>
			log.debug("received innovation confirmation, will update genome. innovation id: {}, from {}, to {}", s.id, s.from, s.to)

			val updatedGenome = t.genome.updateNetworkGenome(s)
			val updatedSettings = t.copy(genome = updatedGenome)

			log.debug("new genome is {}", updatedGenome)
			
			// tell to neuron it has a new Predecessor, tell from neuron it has a new Successor

			val fromActor = generatedActors.allNodes(s.from)
			val toActor = generatedActors.allNodes(s.to)

			toActor.actor ! Neuron.ConnectionConfig(inputs = List(Predecessor(fromActor)))
			fromActor.actor ! Neuron.ConnectionConfig(outputs = List(Successor(toActor)))

			goto(Ready) using updatedSettings

		case Event(s: Innovation.SubnetConnectionInnovationConfirmation, t: NetworkSettings) =>

			log.debug("received innovation confirmation for subnet {}. innovation id: {}, from {}, to {}", s.originalRequest.existingNetId, s.updatedConnectionTracker, s.originalRequest.from, s.originalRequest.to)
			// first we need to re-get the subnet part that we are supposed to be mutating. 
			// then, add the connection to the genome,
			// then create the connections and send to neuron actors. or rather to the subnetwork actor.
			
			val updatedGenome = t.genome.updateSubnet(s)
			val updatedSettings = t.copy(genome = updatedGenome)

			val newlyupdatedsubnetGenome = updatedGenome.subnets.get.filter(sn => sn.id == s.updatedNetTracker )(0)
			val newlyupdatedconnectionGenome = newlyupdatedsubnetGenome.connections.filter(cn=> cn.id == s.updatedConnectionTracker)(0)
			log.debug("updated the subnet and network genome now: {}, going to send to node {} which is {}", updatedGenome, s.originalRequest.neuronId, generatedActors.allNodes(s.originalRequest.neuronId).actor)

			generatedActors.allNodes(s.originalRequest.neuronId).actor ! SubNetwork.ConnectionUpdate(newlyupdatedsubnetGenome,newlyupdatedconnectionGenome)

			goto(Ready) using updatedSettings
	}

}
