package com.thingy.agent

import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.genome.{NetworkGenomeBuilder, NetworkGenome}
import com.thingy.network.Network
import com.thingy.innovation._
import com.thingy.neuron.Neuron

sealed trait AgentState
case object Initialising extends AgentState
case object Active extends AgentState



object Agent {

	case class AgentSettings(
		genome: NetworkGenome.NetworkGenome)

	def props(innovation: ActorRef, network: NetworkGenomeBuilder): Props = {
		Props(classOf[Agent], innovation, network)
	}


}



class Agent(innovation: ActorRef, networkGenomeBuilder: NetworkGenomeBuilder) extends FSM[AgentState, Agent.AgentSettings] {
	import Agent._
	val networkGenome = networkGenomeBuilder.generateFromSeed
	val network = context.actorOf(Network.props("my network", networkGenome), "mynetwork")
	network ! Neuron.Signal(10)
 	innovation ! Innovation.NetworkConnectionInnovation(1,3)


 	startWith(Active, AgentSettings(genome = networkGenome))

	when(Active) {

		case Event(s: Innovation.InnovationConfirmation, t: AgentSettings) =>
			log.debug("received innovation confirmation, will update genome.")
			stay using t
	}
}
