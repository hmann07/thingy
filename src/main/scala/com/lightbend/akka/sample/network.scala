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
import com.thingy.environment.Environment.Representation
import com.thingy.evaluator._

sealed trait NetworkState
case object Initialising extends NetworkState
case object Ready extends NetworkState
case object Active extends NetworkState
case object Mutating extends NetworkState



// State Data holder
final case class NetworkSettings(
		id: Int = 1, 
		genome:  NetworkGenome.NetworkGenome, 
		networkSchema: NetworkGenome.NetworkNodeSchema,
		rCount: Int = 0,
		expectedOutputs: Map[Int, Representation] = Map.empty, // Map[Batchid, Representation]
		actualOutputs: Map[Int, Map[Int, Double]] = Map.empty, // Map[Batchid, Map[OuputputId, ActualOutputValue]]
		evaluator: Evaluator = XOREvaluator()
		 )

object Network {

	// Messages it can receive
	final case class Signal(value: Double)
	final case class Mutate()
	final case class Perceive()
	case class Mutated()
	case class NetworkUpdate(f: () => NetworkGenome.NetworkGenome)
	case class Done(performanceValue: Double, genome: NetworkGenome.NetworkGenome)
    // an override of props to allow Actor to take con structor args.
	// Network should take a genome and create a number of sub networks.

	def props(name: String, networkGenome: ()=> NetworkGenome.NetworkGenome, innovation: ActorRef, environment: ActorRef): Props = {

		Props(classOf[Network], name, networkGenome, innovation, environment)
	}
}

class Network(name: String, ng: ()=> NetworkGenome.NetworkGenome, innovation: ActorRef, environment: ActorRef) extends FSM[NetworkState, NetworkSettings] {

	import Network._
	val networkGenome = ng()

	log.debug("network: {} created with genome {}", name, networkGenome)

	// First create input neurons. Select these on the basis that they belong to layer 0.
	// Input neurons will not have any internal node structures so can be created as straight Neurons.
	// By giving the context the neurons will be created in this context.
	
	val generatedActors: NetworkGenome.NetworkNodeSchema  = networkGenome.generateActors(context, NetworkGenome.NetworkNodeSchema())
	val mutator = new Mutator

	log.debug("Network actors are setup as {} ", generatedActors)

	environment ! Perceive()

	startWith(Ready, NetworkSettings(genome = networkGenome, networkSchema = generatedActors))

	when(Ready) {

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

			log.debug("received representation of {}", r.input)
			
			// Send the signals off into the network, send signal with a batch id (rCount)
			r.input.foreach(i => {
				generatedActors.allNodes(i._1).actor ! Neuron.Signal(i._2, r.flags, t.rCount)
			})

			val updateExpected = t.expectedOutputs + (t.rCount -> r)

			stay using t.copy(rCount = t.rCount + 1, expectedOutputs = updateExpected)

		case Event(s: Neuron.Output, t: NetworkSettings) =>

			log.debug("network received Output signal of {}", s)

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
					context.parent ! Done(ep.fitness, t.genome)
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

		case Event(ng: NetworkUpdate, t: NetworkSettings) =>
      val updatedGenome = ng.f()
			val updatedSchema = updatedGenome.generateActors(context, t.networkSchema)
			val updatedSettings = t.copy(genome = updatedGenome, networkSchema = updatedSchema)
      		log.debug("new network received and actors updated")
      		environment ! Perceive()
			stay using updatedSettings

	}

	onTransition {
		case Mutating -> Ready =>
			context.parent ! Mutated()
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

			toActor.actor ! Neuron.ConnectionConfigUpdate(inputs = List(Predecessor(fromActor)))
			fromActor.actor ! Neuron.ConnectionConfigUpdate(outputs = List(Successor(toActor)))

			goto(Ready) using updatedSettings

		case Event(s: Innovation.SubnetConnectionInnovationConfirmation, t: NetworkSettings) =>

			log.debug("received innovation confirmation for subnet {}. innovation id: {}, from {}, to {}", s.originalRequest.existingNetId, s.updatedConnectionTracker, s.originalRequest.from, s.originalRequest.to)
			// first we need to re-get the subnet part that we are supposed to be mutating. 
			// then, add the connection to the genome,
			// then create the connections and send to neuron actors. or rather to the subnetwork actor.
			
			val updatedGenome = t.genome.updateSubnet(s)
			val updatedSettings = t.copy(genome = updatedGenome)

			val newlyupdatedsubnetGenome = updatedGenome.subnets.get(s.updatedNetTracker)
			val newlyupdatedconnectionGenome = newlyupdatedsubnetGenome.connections(s.updatedConnectionTracker)
			log.debug("updated the subnet and network genome now: {}, going to send to node {} which is {}", updatedGenome, s.originalRequest.neuronId, generatedActors.allNodes(s.originalRequest.neuronId).actor)

			generatedActors.allNodes(s.originalRequest.neuronId).actor ! SubNetwork.ConnectionUpdate(newlyupdatedsubnetGenome,newlyupdatedconnectionGenome)

			goto(Ready) using updatedSettings


		case Event(s: Innovation.NetworkNeuronInnovationConfirmation, t: NetworkSettings) =>
			log.debug("received confirmation of new neuron {}", s)

			val updatedGenome = t.genome.updateNetworkGenome(s)
			// generate the new actor
			val updatedSchema = updatedGenome.generateActors(context, t.networkSchema)
			val updatedSettings = t.copy(genome = updatedGenome, networkSchema = updatedSchema)
			// need to tell two neurons that they have a disabled connection and a new one.
			log.debug("genome updated now: {}, represented as : {}", updatedGenome, updatedGenome.toJson)
			goto(Ready) using updatedSettings

		case Event(s: Innovation.SubNetNeuronInnovationConfirmation, t: NetworkSettings) =>
			log.debug("received confirmation of new neuron {} for subnet", s)

			val updatedGenome = t.genome.updateSubnet(s)
			// generate the new actor
			val updatedSchema = updatedGenome.generateActors(context, t.networkSchema)
			val updatedSettings = t.copy(genome = updatedGenome, networkSchema = updatedSchema)
			// need to tell two neurons that they have a disabled connection and a new one.
			log.debug("genome updated now: {}, represented as : {}", updatedGenome, updatedGenome.toJson)
			goto(Ready) using updatedSettings



	}

}
