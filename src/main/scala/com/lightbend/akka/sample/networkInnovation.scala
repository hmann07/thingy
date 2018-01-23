package com.thingy.innovation

import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.genome._
import scala.collection.immutable.HashMap

sealed trait InnovationState
case object Initialising extends InnovationState
case object Active extends InnovationState

trait Tracker

/*
 * This class is to keep track of the neuron genomes that make up
 * a network part, a modules or, subnetwork.
 * It is used to decide if a subnetwork is different to a different subnetwork.
 * The core way in which this can happen is by analysis of all the neurons that make up the network, essentially by
 * comparing SubNetwork genomes. 
 * Subnetworks are defined by the connections within it.
 * connections are defined by innovation numbers
 * innovation numbers are defined by from and to identifiers of neurons.
 * We must track the neurons and connections of the SubNetwork
 * but also the resulting subnetwork will need to be checked.
 */

object Innovation {

	// messages 

	trait InnovationType
	case class NetworkConnectionInnovation(from: Int, to: Int) extends InnovationType
	case class SubNetConnectionInnovation(from: Int, to: Int, existingStructure: Set[Int], existingNetId: Int, neuronId: Int) extends InnovationType
	case class NetworkNeuronInnovation(connection: NetworkGenome.ConnectionGenome) extends InnovationType
	case class SubNetNeuronInnovation(connection: NetworkGenome.ConnectionGenome, existingStructure: Set[Int], existingNetId: Int, neuronId: Int) extends InnovationType
	
	case class InnovationConfirmation(id: Int, from: Int, to: Int)
	case class SubnetConnectionInnovationConfirmation(updatedNetTracker: Int, updatedConnectionTracker: Int, originalRequest: SubNetConnectionInnovation)
	case class NetworkNeuronInnovationConfirmation(connectionToBeSplit: NetworkGenome.ConnectionGenome, nodeid: Int, priorconnectionId: Int, postconnectionId: Int)
	case class SubNetNeuronInnovationConfirmation(connectionToBeSplit: NetworkGenome.ConnectionGenome, nodeid: Int, subnetId: Int, priorconnectionId: Int, postconnectionId: Int, originalRequest: SubNetNeuronInnovation)
	
	case class SubnetLookupResult(id: Int, tracker: SubnetConnectionTracker)
	case class NetworkLookupResult(id: Int, tracker: NetworkConnectionTracker)
	case class NetworkNeuronLookupResult(id: Int, tracker: NetworkNeuronTracker)
	case class SubNetNeuronLookupResult(id: Int, tracker: SubNetNeuronTracker)
	case class SubnetStructureLookupResult(id: Int, tracker: SubnetTracker)

	abstract class NeuronTracker{val currentInnovationId: Int; val library: Map[Int, Int]}
	abstract class ConnectionTracker{val currentInnovationId: Int; val library: HashMap[String, Int]}

	case class NetworkConnectionTracker(
		val currentInnovationId: Int = 1,
		val library: HashMap[String, Int] = HashMap.empty) extends ConnectionTracker {
		def innovationLookup(s: NetworkConnectionInnovation) = {
			val newKey = s.from + ":" + s.to
			val oldId = currentInnovationId
			val newId = library.getOrElse(newKey, oldId + 1) 
			NetworkLookupResult(newId,
			{if(newId <= oldId) {
					// no update need be done
					this
				} else {
					// new innovation so update library increase the numbers
					this.copy(currentInnovationId = newId, library = library + (newKey -> newId))
				}})
			}} 

	case class SubnetConnectionTracker(
		val currentInnovationId: Int = 1,
		val library: HashMap[String, Int] = HashMap.empty) extends ConnectionTracker {
		def innovationLookup(s: SubNetConnectionInnovation) = {
			val newKey = s.from + ":" + s.to
			val oldId = currentInnovationId
			val newId = library.getOrElse(newKey, oldId + 1) 
			SubnetLookupResult(newId,
			if(newId <= oldId) {
					// no update need be done
					this
				} else {
					// new innovation so update library increase the numbers
					this.copy(currentInnovationId = newId, library = library + (newKey -> newId))
				})}} 

	case class SubnetTracker(
		val currentInnovationId: Int = 1,
		val library: HashMap[Set[Int], Int] = HashMap.empty) extends Tracker {
		def innovationLookup(newSubNetKey: Set[Int]) = {
			val newKey = newSubNetKey
			val oldId = currentInnovationId
			val newId = library.getOrElse(newKey, oldId + 1) 
			SubnetStructureLookupResult(newId,
			if(newId <= oldId) {
					// no update need be done
					this
				} else {
					// new innovation so update library increase the numbers
					this.copy(currentInnovationId = newId, library = library + (newKey -> newId))
				})}}

	case class NetworkNeuronTracker(
		val currentInnovationId: Int = 1,
		val library: HashMap[Int, Int] = HashMap.empty
	) extends NeuronTracker {
		def innovationLookup(connectionToBeSplit: Int) = {
			val oldId = currentInnovationId
			val newId = library.getOrElse(connectionToBeSplit, oldId + 1)
			NetworkNeuronLookupResult(newId,
				if(newId <= oldId) {
					// no update need be done
					this
				} else {
					// new innovation so update library increase the numbers
					this.copy(currentInnovationId = newId, library = library + (connectionToBeSplit -> newId))
				})
		}
	}


	case class SubNetNeuronTracker(
		val currentInnovationId: Int = 1,
		val library: HashMap[Int, Int] = HashMap.empty
	) extends NeuronTracker {
		def innovationLookup(connectionToBeSplit: Int) = {
			val oldId = currentInnovationId
			val newId = library.getOrElse(connectionToBeSplit, oldId + 1)
			SubNetNeuronLookupResult(newId,
				if(newId <= oldId) {
					// no update need be done
					this
				} else {
					// new innovation so update library increase the numbers
					this.copy(currentInnovationId = newId, library = library + (connectionToBeSplit -> newId))
				})
		}
	}


	case class InnovationSettings (

		val networkConnectionTracker: NetworkConnectionTracker = NetworkConnectionTracker(),
		val subnetConnectionTracker: SubnetConnectionTracker = SubnetConnectionTracker(),
		val subnetTracker: SubnetTracker = SubnetTracker(),
		val networkNeuronTracker: NetworkNeuronTracker = NetworkNeuronTracker(),
		val subnetNeuronTracker: SubNetNeuronTracker = SubNetNeuronTracker()
		)
	def props(networkGenome: NetworkGenome.NetworkGenome): Props = {

		Props(classOf[Innovation], networkGenome)
	}

}


class Innovation(networkGenome: NetworkGenome.NetworkGenome) extends FSM[InnovationState, Innovation.InnovationSettings] {
	import Innovation._

	val genome = networkGenome
	val networkConnectionTracker: NetworkConnectionTracker = genome.connections.foldLeft(NetworkConnectionTracker()) { (tracker, current) =>
		val currentVal = current._2
		val updatedLibrary = tracker.library + (currentVal.from + ":" + currentVal.to -> tracker.library.getOrElse(currentVal.from + ":" + currentVal.to, currentVal.id))
		val updatedInnovId = if(currentVal.id > tracker.currentInnovationId) {currentVal.id} else {tracker.currentInnovationId}
		tracker.copy(updatedInnovId, updatedLibrary)
	}

	/* creates a map of from:to -> Innovation Id. 
	 * e.g."1:2" -> 1
	 * This enables fast look up of connections in the map
	 */

	val subnetConnectionTracker: SubnetConnectionTracker = genome.subnets.get.foldLeft(SubnetConnectionTracker()) { (tracker, currentSubnet) =>
		val currentVal1 = currentSubnet._2
		currentVal1.connections.foldLeft(tracker) { (tracker, currentConn) =>
			val currentVal2 = currentConn._2
			val updatedLibrary = tracker.library + (currentVal2.from + ":" + currentVal2.to -> tracker.library.getOrElse(currentVal2.from + ":" + currentVal2.to, currentVal2.id))
			val updatedInnovId = if(currentVal2.id > tracker.currentInnovationId) {currentVal2.id} else {tracker.currentInnovationId}
			tracker.copy(updatedInnovId, updatedLibrary)
		}
	}
	

	/*
	 * The creates an index where by networks are easily identified by their connection innovation ids
	 * An efficient way would be a hash. but this lacks guarentees of non clashes, so will use the set equality tests.
	 * 
	 */
	val subnetTracker: SubnetTracker = genome.subnets.get.foldLeft(SubnetTracker()) { (tracker, current) =>
		val updatedLibrary = tracker.library + (current._2.innovationHash -> current._2.id)
		val updatedInnovId = if(current._2.id > tracker.currentInnovationId) {current._2.id} else {tracker.currentInnovationId}
		tracker.copy(updatedInnovId, updatedLibrary)
	}

	
	val networkNeuronTracker: NetworkNeuronTracker = genome.neurons.foldLeft(NetworkNeuronTracker()) { (tracker, current) =>
		val updatedInnovId = if(current._2.id > tracker.currentInnovationId) {current._2.id} else {tracker.currentInnovationId}
		tracker.copy(currentInnovationId = updatedInnovId)
	}

	val subnetNeuronTracker: SubNetNeuronTracker = genome.subnets.get.foldLeft(SubNetNeuronTracker()) { (tracker, currentSubnet) =>
		val currentVal1 = currentSubnet._2
		currentVal1.neurons.foldLeft(tracker) { (tracker, currentNeuron) =>
			val updatedInnovId = if(currentNeuron._2.id > tracker.currentInnovationId) {currentNeuron._2.id} else {tracker.currentInnovationId}
		    tracker.copy(currentInnovationId = updatedInnovId)
	}} 
	
	

	log.debug(
		"\n innovation tracker started. networkConnectionTracker tracker is: {}\n" +
		" subnetConnectionTracker tracker is {}\n" +
		" net neurons are {}\n" + 
		" sub net neurons are {}\n", networkConnectionTracker, subnetConnectionTracker, networkNeuronTracker, subnetNeuronTracker)

	startWith(Active, InnovationSettings(networkConnectionTracker, subnetConnectionTracker, subnetTracker, networkNeuronTracker, subnetNeuronTracker))

	when(Active) {

		case Event(s: NetworkConnectionInnovation, t: InnovationSettings) =>
			log.debug("received request to create connection between {} and {}", s.from, s.to)
			val updatedTracker = t.networkConnectionTracker.innovationLookup(s)
			sender() ! InnovationConfirmation(updatedTracker.id, s.from, s.to)
			stay using t.copy(networkConnectionTracker = updatedTracker.tracker)

		case Event(s: SubNetConnectionInnovation, t: InnovationSettings) =>
			log.debug("received request to create connection between {} and {} of subnetwork", s.to, s.from)
			val updatedTracker = t.subnetConnectionTracker.innovationLookup(s)
			val updatedNetTracker = t.subnetTracker.innovationLookup(s.existingStructure + updatedTracker.id)
			sender() ! SubnetConnectionInnovationConfirmation(updatedNetTracker.id, updatedTracker.id, s)
			stay using t.copy(subnetConnectionTracker = updatedTracker.tracker, subnetTracker = updatedNetTracker.tracker)

		case Event(s: NetworkNeuronInnovation, t: InnovationSettings) =>
			log.debug("received request to create a new Neuron for connection {}", s.connection)
			val updatedTracker = t.networkNeuronTracker.innovationLookup(s.connection.id)
			val lookup1 = t.networkConnectionTracker.innovationLookup(NetworkConnectionInnovation(s.connection.from, updatedTracker.id))
			val lookup2 = lookup1.tracker.innovationLookup(NetworkConnectionInnovation(updatedTracker.id, s.connection.to))
			sender() ! NetworkNeuronInnovationConfirmation(s.connection, updatedTracker.id, lookup1.id, lookup2.id)
			stay using t.copy(networkNeuronTracker = updatedTracker.tracker, networkConnectionTracker = lookup2.tracker)

		case Event(s: SubNetNeuronInnovation, t: InnovationSettings) =>
			log.debug("received request to create a new Neuron for connection {} from neuron: {} to neruon {} within subnet {}", s.connection.id, s.connection.from, s.connection.to, s.existingNetId)
			log.debug("neuron tracker is currently: {}", t.subnetNeuronTracker)
			val updatedTracker = t.subnetNeuronTracker.innovationLookup(s.connection.id)
			log.debug("neuron tracker updated to: {}, will use innovation id {}", updatedTracker, updatedTracker.id)
			log.debug("connection tracker is currently: {}", t.subnetConnectionTracker)
			val lookup1 = t.subnetConnectionTracker.innovationLookup(SubNetConnectionInnovation(s.connection.from, updatedTracker.id, s.existingStructure, s.existingNetId, s.neuronId))
			val lookup2 = lookup1.tracker.innovationLookup(SubNetConnectionInnovation(updatedTracker.id, s.connection.to, s.existingStructure, s.existingNetId, s.neuronId))
			
			log.debug("connection tracker is updated to: {} is for prior and post are {} and {}", lookup2.tracker, lookup1.id, lookup2.id)

			val updatedNetTracker = t.subnetTracker.innovationLookup(s.existingStructure + lookup1.id + lookup2.id)

			sender() ! SubNetNeuronInnovationConfirmation(s.connection, updatedTracker.id, updatedNetTracker.id, lookup1.id, lookup2.id, s)
			stay using t.copy(subnetNeuronTracker = updatedTracker.tracker, subnetConnectionTracker = lookup2.tracker, subnetTracker = updatedNetTracker.tracker)
	}
}
	