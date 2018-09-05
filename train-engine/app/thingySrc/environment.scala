package com.thingy.environment

import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.network.Network.Perceive

import scala.io.Source

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
	val fieldmap = List("Input", "Input", "Output")
	val filename = "Xor.json"
	val fileNameAndPath = "C:\\Windows\\Temp\\" + filename
	val lines = Source.fromFile(fileNameAndPath).getLines
	val representations  = lines.map { line =>
		
		val columns: List[String] = line.split(",").toList
		val zippedCols = columns.zip(fieldmap)
		val (inputs, idx) = zippedCols.foldLeft((Map[Int, Double](), 1)) { (acc, current) =>

			if(current._2=="Input"){

			(acc._1 + (acc._2 -> current._1.toDouble), acc._2 + 1)
			} else {
			(acc._1, acc._2 )
			}
		}

		val (outputs, idx2) = zippedCols.foldLeft((Map[Int, Double](), idx)) { (acc, current) =>
			if(current._2=="Output"){
			(acc._1 + (acc._2 -> current._1.toDouble), acc._2 + 1)
			} else {
			(acc._1, acc._2)
			}
		}
    
    	if(lines.hasNext) {
				Representation(List.empty, inputs, outputs)
    	}else{
    		Representation(List("FINAL"), inputs, outputs)
    	}

	}


	val experienceStream = Experience(representations.toList)


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