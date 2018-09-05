package com.thingy.agent

import com.thingy.config.ConfigDataClass.ConfigData
import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.genome.{NetworkGenomeBuilder, NetworkGenome,GenomeIO}
import com.thingy.network.{Network, NetworkTest, NetworkActive}
import com.thingy.innovation._
import com.thingy.neuron.Neuron
import com.thingy.environment.Environment


sealed trait AgentState
case object Initialising extends AgentState
case object ActiveAgent extends AgentState
case object TestState extends AgentState



object Agent {

	case class AgentSettings()
	case class Performance(performanceValue: Double, genome: NetworkGenome)
	
	def props(innovation: ActorRef, network: GenomeIO, configData: ConfigData, startState: AgentState, fieldmap: List[String], out: ActorRef = null): Props = {
		
		Props(classOf[Agent], innovation, network, configData, startState, fieldmap, out)
	}

}



class Agent(innovation: ActorRef, ng: GenomeIO, configData: ConfigData, startState: AgentState, fieldmap: List[String], out: ActorRef = null) extends FSM[AgentState, Agent.AgentSettings] {
	import Agent._
	def networkGenome = ng match {
		case GenomeIO(None, None) => {
			
			val cg = ng.generateFromSpec(fieldmap)
			log.info("creating genome from spec " + cg)
			cg
		}
		case _ => ng.generate
	}
	 
	val environment = context.actorOf(Environment.props(), "environment")
	val network = startState match {
		case TestState =>
			context.actorOf(Network.props("my network", networkGenome, innovation, environment, configData, NetworkTest, out), "mynetwork")
		case ActiveAgent => 
			context.actorOf(Network.props("my network", networkGenome, innovation, environment, configData, NetworkActive, null), "mynetwork")
		}


 	startWith(startState, AgentSettings())

 	when(ActiveAgent) {
 		case Event(g: Network.Mutated, t: AgentSettings) =>
 			log.debug("netowork finished mutating.")

 			stay

 		case Event(g: Network.Done, t: AgentSettings) =>
 			
 			context.parent ! g

 			stay

 		case Event(ng: Network.NetworkUpdate , s: AgentSettings) =>

 			log.debug("received network")
 			network ! ng
 			stay
 	}

 	when(TestState){

 		case Event(g: Network.Completed, t: AgentSettings) =>
 			
 			println("TestComplete")
 			//context.parent ! g
 			context.stop(self)
 			stay


 	}

 	

}
