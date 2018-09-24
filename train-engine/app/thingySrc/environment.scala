package com.thingy.environment

import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.network.Network.Perceive
import com.thingy.environment.EnvironmentIOSpec

import scala.io.Source

sealed trait EnvironmentState
case object Initialising extends EnvironmentState
case object Active extends EnvironmentState

object Environment {

	case class EnvironmentSettings(next: Representation, queued: List[Representation])
	case class Representation(flags: List[String], input: Map[Int, Double], expectedOutput: Map[Int, Double])
	

	case class Experience(representations: List[Representation])
	
	def props(envType: EnvironmentType ): Props = {
		Props(classOf[Environment], envType)
	}
}



class Environment(envType: EnvironmentType) extends FSM[EnvironmentState, Environment.EnvironmentSettings] {
	import Environment._


	val experienceStream: Experience = envType match {
		case et: FileEnvironmentType =>
				val filename = et.environmentConnetion
				val fileNameAndPath = "C:\\Windows\\Temp\\" + filename
				val lines = Source.fromFile(fileNameAndPath).getLines
				val representations  = lines.map { line =>
					
					val columns: List[String] = line.split(",").toList
					
					val inputMap = et.environmentIOSpec.inputs.foldLeft(Map[Int, Double]()) { (acc, current) =>
						acc + (current -> columns(current-1).toDouble)
					}

					val outputMap = et.environmentIOSpec.outputs.foldLeft(Map[Int, Double]()) { (acc, current) =>
						acc + (current -> columns(current-1).toDouble)
					}

			    
			    	if(lines.hasNext) {
							Representation(List.empty, inputMap, outputMap)
			    	}else{
			    		Representation(List("FINAL"), inputMap, outputMap)
			    	}

				}


				Experience(representations.toList)
		case _ =>
			Experience(List.empty)
	}

	
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