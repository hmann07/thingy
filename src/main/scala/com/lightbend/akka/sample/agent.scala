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

	case class AgentSettings()

	def props(innovation: ActorRef, network: NetworkGenomeBuilder): Props = {
		Props(classOf[Agent], innovation, network)
	}


}



class Agent(innovation: ActorRef, networkGenomeBuilder: NetworkGenomeBuilder) extends FSM[AgentState, Agent.AgentSettings] {
	import Agent._
	val networkGenome = networkGenomeBuilder.generateFromSeed
	val network = context.actorOf(Network.props("my network", networkGenome), "mynetwork")
	network ! Neuron.Signal(10)
 	innovation ! Innovation.NetworkConnectionInnovation(3,3)


 	startWith(Active, AgentSettings())

	when(Active) {

			
		case Event(s: Innovation.InnovationConfirmation, t: AgentSettings) =>
			log.debug("received innovation confirmation, will update genome. innovation id: {}, from {}, to {}", s.id, s.from, s.to)
			network ! s
			stay using t
			
	}
}
