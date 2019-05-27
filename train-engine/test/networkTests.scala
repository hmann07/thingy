package com.thingy.tests

import akka.testkit.TestFSMRef
import akka.actor.FSM



import org.scalatestplus.play._
import org.scalatest._
import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit, TestActorRef, TestProbe }
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory

import com.thingy.weight.Weight
import com.thingy.node._
import com.thingy.neuron.{Predecessor, Successor, Neuron, Initialising, Ready}
import com.thingy.genome.NeuronGenome
import com.thingy.network._
import com.thingy.genome.ConnectionGenome
import com.thingy.genome.GenomeIO
import com.thingy.genome.NetworkGenomeBuilder
import com.thingy.innovation.Innovation
import com.thingy.config.ConfigDataClass.ConfigData
import com.thingy.environment._
import com.thingy.evaluator.SSEEvaluator

import reactivemongo.bson.BSONDocument
import reactivemongo.play.json._
import play.modules.reactivemongo.json.collection._


class TestNetwork extends FlatSpec {

	val config = ConfigFactory.load()

    val envSpec = EnvironmentIOSpec(List(1,2), List(3))
	implicit val system = ActorSystem()
	val envType: FileEnvironmentType = new FileEnvironmentType(BSONDocument(
      "name" -> "environmentName",
      "fieldmap" -> List("Input", "Input", "Output"),
      "filename" -> "Xor.json"))
	val networkGenome = GenomeIO(None, None).generateFromSpec(envSpec)
	val innovation = TestFSMRef(new Innovation(networkGenome), "innov8")
	val environment = TestFSMRef(new Environment(envType), "environment")
	val evaluator = SSEEvaluator(fieldMap = envType.environmentIOSpec)

	val networkActor = TestFSMRef(
								new Network(
									"my network", 
									networkGenome, 
									innovation, 
									environment, 
									ConfigData(), 
									NetworkActive, 
									evaluator, 
									null), "mynetwork")


	

	
	"the newotk" should "have three children" in {
		
		val c = networkActor.children.size

		assert(c == 3)
	}

	

}