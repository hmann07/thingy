package com.thingy.agent

import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.genome.{NetworkGenomeBuilder, NetworkGenome}
import com.thingy.network.Network
import com.thingy.innovation._
import com.thingy.neuron.Neuron
import com.thingy.environment.Environment


sealed trait AgentState
case object Initialising extends AgentState
case object Active extends AgentState




object Agent {

	case class AgentSettings()
	case class Performance(performanceValue: Double, genome: NetworkGenome.NetworkGenome)
	
	def props(innovation: ActorRef, network: ()=> NetworkGenome.NetworkGenome): Props = {
		Props(classOf[Agent], innovation, network)
	}
}



class Agent(innovation: ActorRef, ng: ()=> NetworkGenome.NetworkGenome) extends FSM[AgentState, Agent.AgentSettings] {
	import Agent._



	val environment = context.actorOf(Environment.props(), "environment")
	val networkGenome = ng
	val network = context.actorOf(Network.props("my network", networkGenome, innovation, environment), "mynetwork")

 	startWith(Active, AgentSettings())

 	when(Active) {
 		case Event(g: Network.Mutated, t: AgentSettings) =>
 			log.debug("netowork finished mutating.")

 			stay

 		case Event(g: Network.Done, t: AgentSettings) =>
 			
 			//network ! Network.Mutate()
 			context.parent ! g

 			stay

 		case Event(ng: Network.NetworkUpdate , s: AgentSettings) =>

 			log.debug("received network")
 			network ! ng
 			stay
 	}
}
