package com.thingy.environment

import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.network.Network.Perceive

sealed trait EnvironmentState
case object Initialising extends EnvironmentState
case object Active extends EnvironmentState

object Environment {

	case class EnvironmentSettings(next: Representation, queued: List[Representation])
	case class Representation(flags: List[String], input: Map[Int, Double], expectedOutput: Map[Int, Double])
	

	case class Experience(representations: List[Representation])
	def props(): Props = {
		Props(classOf[Environment])
	}
}



class Environment() extends FSM[EnvironmentState, Environment.EnvironmentSettings] {
	import Environment._
	val experienceStream = Experience(List(
										Representation(List.empty, Map(1->1, 2->0), Map(3 -> 1)),
										Representation(List.empty, Map(1->0, 2->1), Map(3 -> 1)),
										Representation(List.empty, Map(1->1, 2->1), Map(3 -> 0)),
										Representation(List("FINAL"), Map(1 -> 0, 2 -> 0), Map(3 -> 0))
									))

	startWith(Active, EnvironmentSettings(experienceStream.representations.head, experienceStream.representations.tail))

 	when(Active) {
 		case Event(g: Perceive, t: EnvironmentSettings) =>
 			val s = sender()
 			s ! t.next
 			t.queued.headOption match {
 				case Some(h) =>  stay using t.copy(next = h, queued = t.queued.tail)
 				case None => stay using t.copy(experienceStream.representations.head, experienceStream.representations.tail)
 			}
 			
 	}
}