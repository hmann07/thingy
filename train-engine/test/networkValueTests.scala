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
import com.thingy.genome.NetworkGenome
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





class TestValueNetwork extends TestKit(ActorSystem("MySpec")) with ImplicitSender  with Matchers with FlatSpecLike with BeforeAndAfterAll  {
	

	val config = ConfigFactory.load()

    val envSpec = EnvironmentIOSpec(List(1,2), List(3))
	//implicit val system = ActorSystem()
	val envType: FileEnvironmentType = new FileEnvironmentType(BSONDocument(
      "name" -> "environmentName",
      "fieldmap" -> List("Input", "Input", "Output"),
      "filename" -> "Xor.json"))


	val testGenome = 
		NetworkGenome(1,2,1,
			Map(
				1 -> NeuronGenome(1,"inputNeuron1",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
				2 -> NeuronGenome(2,"inputNeuron2",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
				3 -> NeuronGenome(3,"outputNeuron3",1.0,true,true,Some("SIGMOID"),Some(1),Weight(1),"output")),

			Map(1 -> ConnectionGenome(1,1,3,Weight(1),true,true,true,false), 
				2 -> ConnectionGenome(2,2,3,Weight(1),true,true,true,false)),

			Some(Map(
				1 -> NetworkGenome(1,1,1,Map(
					1 -> NeuronGenome(1,"subnetIputNeuron1",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
					2 -> NeuronGenome(2,"outputNeuron2",1.0,true,true,Some("SIGMOID"),None,Weight(1),"output")),
				Map(1 -> ConnectionGenome(1,1,2,Weight(1),true,true,true,false)),
				None,Some(3),0,0))),Some(0),0,0)


	val networkGenome = GenomeIO(None, Some(()=>testGenome)).generate
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
	



	
	"the network" should "have one child" in {
		
		val c = networkActor.children.size

		awaitCond(c == 1, 2000 millis, 100 millis) 
	}

	"the network schema" should "have 2 input nodes" in {
		val nodeS = networkActor.stateData.networkSchema.in.nodes.size
		awaitCond(nodeS == 2, 2000 millis, 100 millis) 
		//assert(nodeS == 2)
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
			agent.expectMsg(9000.millis, Network.Done(
			SSEEvaluator(0,0.25,4.0,0.25,EnvironmentIOSpec(List(2, 1),List(3))), NetworkGenome(1,2,1,
			Map(
				1 -> NeuronGenome(1,"inputNeuron1",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
				2 -> NeuronGenome(2,"inputNeuron2",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
				3 -> NeuronGenome(3,"outputNeuron3",1.0,true,true,Some("SIGMOID"),Some(1),Weight(1),"output")),

			Map(1 -> ConnectionGenome(1,1,3,Weight(1),true,true,true,false), 
				2 -> ConnectionGenome(2,2,3,Weight(1),true,true,true,false)),

			Some(Map(
				1 -> NetworkGenome(1,1,1,Map(
					1 -> NeuronGenome(1,"subnetIputNeuron1",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
					2 -> NeuronGenome(2,"outputNeuron2",1.0,true,true,Some("SIGMOID"),None,Weight(1),"output")),
				Map(1 -> ConnectionGenome(1,1,2,Weight(1),true,true,true,false)),
				None,Some(3),0,0))),Some(0),0,0) ))
		}
	}

	within(0.millis, 9000.millis) {
		"With slightly different weight values the network" should "receive an Done message from the network with correct value" in {
			//Representation(flags: List[String], input: Map[Int, Double], expectedOutput: Map[Int, Double])
			val testGenome = 
		NetworkGenome(1,2,1,
			Map(
				1 -> NeuronGenome(1,"inputNeuron1",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
				2 -> NeuronGenome(2,"inputNeuron2",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
				3 -> NeuronGenome(3,"outputNeuron3",1.0,true,true,Some("SIGMOID"),Some(1),Weight(1),"output")),

			Map(1 -> ConnectionGenome(1,1,3,Weight(0.5),true,true,true,false), 
				2 -> ConnectionGenome(2,2,3,Weight(0.5),true,true,true,false)),

			Some(Map(
				1 -> NetworkGenome(1,1,1,Map(
					1 -> NeuronGenome(1,"subnetIputNeuron1",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
					2 -> NeuronGenome(2,"outputNeuron2",1.0,true,true,Some("SIGMOID"),None,Weight(1),"output")),
				Map(1 -> ConnectionGenome(1,1,2,Weight(1),true,true,true,false)),
				None,Some(3),0,0))),Some(0),0,0)
			val networkGenome = GenomeIO(None, Some(()=>testGenome)).generate

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
			agent.expectMsg(9000.millis, Network.Done(
			SSEEvaluator(0,0.1425369565965509,7.015724369859303,0.1425369565965509,EnvironmentIOSpec(List(2, 1),List(3))), NetworkGenome(1,2,1,
			Map(
				1 -> NeuronGenome(1,"inputNeuron1",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
				2 -> NeuronGenome(2,"inputNeuron2",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
				3 -> NeuronGenome(3,"outputNeuron3",1.0,true,true,Some("SIGMOID"),Some(1),Weight(1),"output")),

			Map(1 -> ConnectionGenome(1,1,3,Weight(0.5),true,true,true,false), 
				2 -> ConnectionGenome(2,2,3,Weight(0.5),true,true,true,false)),

			Some(Map(
				1 -> NetworkGenome(1,1,1,Map(
					1 -> NeuronGenome(1,"subnetIputNeuron1",0.0,true,true,Some("SIGMOID"),None,Weight(1),"input"), 
					2 -> NeuronGenome(2,"outputNeuron2",1.0,true,true,Some("SIGMOID"),None,Weight(1),"output")),
				Map(1 -> ConnectionGenome(1,1,2,Weight(1),true,true,true,false)),
				None,Some(3),0,0))),Some(0),0,0) ))
		}
	}

	


	
}