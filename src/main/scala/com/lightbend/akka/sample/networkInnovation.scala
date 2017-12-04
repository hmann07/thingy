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

	
	case class NetworkConnectionInnovation(from: Int, to: Int) 
	case class SubNetConnectionInnovation(from: Int, to: Int, existingStructure: Set[Int], existingNetId: Int, neuronId: Int) 
	case class NetworkNeuronInnovation(from: Int, to: Int)
	case class SubNetNeuronInnovation(from: Int, to: Int)
	case class InnovationConfirmation(id: Int, from: Int, to: Int)
	case class SubnetConnectionInnovationConfirmation(updatedNetTracker: Int, updatedConnectionTracker: Int, originalRequest: SubNetConnectionInnovation)
	case class SubnetLookupResult(id: Int, tracker: SubnetConnectionTracker)
	case class NetworkLookupResult(id: Int, tracker: NetworkConnectionTracker)
	case class SubnetStructureLookupResult(id: Int, tracker: SubnetTracker)

	abstract class ConnectionTracker{val currentInnovationId: Int; val library: HashMap[String, Int]}
	case class NetworkConnectionTracker(
		val currentInnovationId: Int = 1,
		val library: HashMap[String, Int] = HashMap.empty) extends ConnectionTracker {
		def innovationLookup(s: NetworkConnectionInnovation) = {
			val newKey = s.from + ":" + s.to
			val oldId = currentInnovationId
			val newId = library.getOrElse(newKey, oldId + 1) 
			NetworkLookupResult(newId,
			if(newId == oldId) {
					// no update need be done
					this
				} else {
					// new innovation so update library increase the numbers
					this.copy(currentInnovationId = newId, library = library + (newKey -> newId))
				})}} 

	case class SubnetConnectionTracker(
		val currentInnovationId: Int = 1,
		val library: HashMap[String, Int] = HashMap.empty) extends ConnectionTracker {
		def innovationLookup(s: SubNetConnectionInnovation) = {
			val newKey = s.from + ":" + s.to
			val oldId = currentInnovationId
			val newId = library.getOrElse(newKey, oldId + 1) 
			SubnetLookupResult(newId,
			if(newId == oldId) {
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
			if(newId == oldId) {
					// no update need be done
					this
				} else {
					// new innovation so update library increase the numbers
					this.copy(currentInnovationId = newId, library = library + (newKey -> newId))
				})}}


	case class InnovationSettings (

		val networkConnectionTracker: NetworkConnectionTracker = NetworkConnectionTracker(),
		val subnetConnectionTracker: SubnetConnectionTracker = SubnetConnectionTracker(),
		val subnetTracker: SubnetTracker = SubnetTracker()
		)
	def props(networkGenome: NetworkGenomeBuilder): Props = {

		Props(classOf[Innovation], networkGenome)
	}

}


class Innovation(networkGenome: NetworkGenomeBuilder) extends FSM[InnovationState, Innovation.InnovationSettings] {
	import Innovation._

	val genome = networkGenome.generateFromSeed
	val networkConnectionTracker = genome.connections.foldLeft(NetworkConnectionTracker()) { (tracker, current) =>
		val updatedLibrary = tracker.library + (current.from + ":" + current.to -> tracker.library.getOrElse(current.from + ":" + current.to, current.id))
		val updatedInnovId = if(current.id > tracker.currentInnovationId) {current.id} else {tracker.currentInnovationId}
		tracker.copy(updatedInnovId, updatedLibrary)
	}

	/* creates a map of from:to -> Innovation Id. 
	 * e.g."1:2" -> 1
	 * This enables fast look up of connections in the map
	 */

	val subnetConnectionTracker = genome.subnets.get.foldLeft(SubnetConnectionTracker()) { (tracker, currentSubnet) =>
		currentSubnet.connections.foldLeft(tracker) { (tracker, currentConn) =>
			val updatedLibrary = tracker.library + (currentConn.from + ":" + currentConn.to -> tracker.library.getOrElse(currentConn.from + ":" + currentConn.to, currentConn.id))
			val updatedInnovId = if(currentConn.id > tracker.currentInnovationId) {currentConn.id} else {tracker.currentInnovationId}
			tracker.copy(updatedInnovId, updatedLibrary)
		}
	}
	

	/*
	 * The creates an index where by networks are easily identified by their connection innovation ids
	 * An efficient way would be a hash. but this lacks guarentees of non clashes, so will use the set equality tests.
	 * 
	 */
	val subnetTracker = genome.subnets.get.foldLeft(SubnetTracker()) { (tracker, current) =>
			val updatedLibrary = tracker.library + (current.innovationHash -> current.id)
			val updatedInnovId = if(current.id > tracker.currentInnovationId) {current.id} else {tracker.currentInnovationId}
			tracker.copy(updatedInnovId, updatedLibrary)
		}

	


	log.debug(
		"\n innovation tracker started. networkConnectionTracker tracker is: {}\n" +
		" subnetConnectionTracker tracker is {}\n" +
		" subnets are {}", networkConnectionTracker, subnetConnectionTracker, subnetTracker)

	startWith(Active, InnovationSettings(networkConnectionTracker, subnetConnectionTracker, subnetTracker))

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
	}
}
	