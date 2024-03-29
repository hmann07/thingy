package com.thingy.tests

import akka.testkit.TestFSMRef
import akka.actor.FSM



import org.scalatestplus.play._
import org.scalatest._
import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit, TestActorRef, TestProbe, DefaultTimeout }
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory

import com.thingy.weight.Weight
import com.thingy.node._
import com.thingy.neuron.{Predecessor, Successor, Neuron, Initialising, Ready}
import com.thingy.genome.NeuronGenome
import com.thingy.network._
import com.thingy.messages.Perceive
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





class TestNetwork extends TestKit(ActorSystem("MySpec")) with ImplicitSender  with Matchers with FlatSpecLike with BeforeAndAfterAll  {
	

	val config = ConfigFactory.load()

    val envSpec = EnvironmentIOSpec(List(1,2), List(3))
	//implicit val system = ActorSystem()
	val envType: FileEnvironmentType = new FileEnvironmentType(BSONDocument(
      "name" -> "environmentName",
      "fieldmap" -> List("Input", "Input", "Output"),
      "filename" -> "Xor.json"))
	val networkGenome = GenomeIO(None, None).generateFromSpec(envSpec)
	val innovation = TestProbe()
	val environment = TestProbe()
	val evaluator = SSEEvaluator(fieldMap = envType.environmentIOSpec)

	
	
	val networkActor = TestFSMRef(
								new Network(
									"my network", 
									networkGenome, 
									innovation.ref, 
									environment.ref, 
									ConfigData(), 
									NetworkActive, 
									evaluator, 
									null), "mynetwork")


	within(0.millis, 9000.millis) {
	"env" should 
		"get perceive " in {
			environment.expectMsg(9000.millis, Perceive())
		}
	}
	



	
	"the network" should "have 1 child" in {
		
		val c = networkActor.children.size

		awaitCond(c == 1, 2000 millis, 100 millis) 
	}

	"the network schema" should "have 2 input nodes" in {
		val nodeS = networkActor.stateData.networkSchema.in.nodes.size
		awaitCond(nodeS == 2, 2000 millis, 100 millis) 
		//assert(nodeS == 2)
	}

	"the network schema" should "have 1 output nodes" in {
		val nodeS = networkActor.stateData.networkSchema.out.nodes.size
		awaitCond(nodeS == 1, 2000 millis, 100 millis) 
		//assert(nodeS == 2)
	}
	"the network schema" should "have 0 hidden nodes" in {
		val nodeS = networkActor.stateData.networkSchema.hidden.nodes.size
		awaitCond(nodeS == 0, 2000 millis, 100 millis) 
		//assert(nodeS == 2)
	}

	"the network schema" should "have 3 nodes in total" in {
		val nodeS = networkActor.stateData.networkSchema.allNodes.size
		awaitCond(nodeS == 3, 2000 millis, 100 millis) 
		//assert(nodeS == 2)
	}

	"the network" should "have a list of neurons connected to an input" in {
		awaitCond(networkActor.stateData.networkSchema.inNodes.size == 2, 2000 millis, 100 millis)
	}

	within(0.millis, 9000.millis) {
		"the network" should "receive an Done message from the network" in {
			//Representation(flags: List[String], input: Map[Int, Double], expectedOutput: Map[Int, Double])
			val representation =  com.thingy.environment.Environment.Representation(List("FINAL"), Map(1 -> 1, 2 -> 0), Map(3 -> 0))
			val agent = TestProbe()
			val net = agent.childActorOf(Network.props("my network2", 
									networkGenome, 
									innovation.ref, 
									environment.ref, 
									ConfigData(), 
									NetworkActive, 
									evaluator, 
									null), "mynetwork2")
			agent.send(net, representation) 
			agent.expectMsgType[Network.Done](9000.millis)
		}
	}





	
}