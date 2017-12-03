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
	val network = context.actorOf(Network.props("my network", networkGenome, innovation), "mynetwork")
	network ! Neuron.Signal(10)
	network ! Network.Mutate()
 
 	startWith(Active, AgentSettings())

}
