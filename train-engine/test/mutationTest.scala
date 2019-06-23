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





class TestMutation extends TestKit(ActorSystem("MySpec")) with ImplicitSender  with Matchers with FlatSpecLike with BeforeAndAfterAll  {
	

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

	//connectionMutation = Innovation.InnovationConfirmation(id = 3, from , to: Int)
	
	val neuronMutation = Innovation.NetworkNeuronInnovationConfirmation(connectionToBeSplit= ConnectionGenome(1,1,3,Weight(1),true,true,true,false), nodeid= 4, priorconnectionId= 3, postconnectionId= 4)
	val agent = TestProbe()
			val networkActor = agent.childActorOf(Network.props("my network2", 
									networkGenome, 
									innovation.ref, 
									environment.ref, 
									ConfigData(), 
									NetworkActive, 
									evaluator, 
									null), "mynetwork2")
	/*val networkActor = TestFSMRef(
								new Network(
									"my network", 
									networkGenome, 
									innovation.ref, 
									environment.ref, 
									ConfigData(), 
									NetworkActive, 
									evaluator, 
									null), "mynetwork")*/
	
	val statenetworkActor = TestFSMRef(
								new Network(
									"my network", 
									networkGenome, 
									innovation.ref, 
									environment.ref, 
									ConfigData(), 
									NetworkActive, 
									evaluator, 
									null), "mynetwork")


	networkActor ! Network.Mutate()
	networkActor ! neuronMutation

	statenetworkActor ! Network.Mutate()
	statenetworkActor ! neuronMutation

	"the network schema" should "have 2 input nodes" in {
		
		val nodeS = statenetworkActor.stateData.networkSchema.in.nodes.size
		awaitCond(nodeS == 2, 2000 millis, 100 millis) 
		//assert(nodeS == 2)
	}

	"the network schema" should "have 1 hidden node" in {
		
		val nodeS = statenetworkActor.stateData.networkSchema.hidden.nodes.size
		awaitCond(nodeS == 1, 2000 millis, 100 millis) 
		//assert(nodeS == 2)
	}

	"the network" should "have 2 neurons" in {
		
	
		val neurons = statenetworkActor.children.size


		awaitCond(neurons == 2, 2000 millis, 100 millis) 
	}

	"the network" should "have 1 and 2  as an input neuron" in {
		
	
		val inNodes = statenetworkActor.stateData.networkSchema.inNodes
		//	println(inNodes)
		awaitCond(inNodes.keys == Set(1,2), 2000 millis, 100 millis) 
	}

	"network input 1" should "have 1 input neuron" in {
		
	
		
		val inNodes = statenetworkActor.stateData.networkSchema.inNodes(1).size

		(inNodes == 1, 2000 millis, 100 millis) 
	}



	within(0.millis, 9000.millis) {
		"the network" should "receive an Done message from the network" in {
			//Representation(flags: List[String], input: Map[Int, Double], expectedOutput: Map[Int, Double])
			val representation =  com.thingy.environment.Environment.Representation(List("FINAL"), Map(1 -> 1, 2 -> 0), Map(3 -> 1))
			
			agent.send(networkActor, representation) 
			val expectedMessage = agent.expectMsgPF(){ 
				case ok@Network.Done(
			e: SSEEvaluator, g: NetworkGenome) => ok
			}

			val newConnWeight3 = expectedMessage.genome.connections(3).weight.value
			val newConnWeight4 = expectedMessage.genome.connections(4).weight.value

			val newNeuronBiasWeight = expectedMessage.genome.neurons(4).biasWeight.value
			
			val netval = Math.pow(1 - (1 / (1 + Math.exp(-(1 / (1 + Math.exp(-((1*newConnWeight3) + (-1* newNeuronBiasWeight))) + (0 * 1)) + -1)))), 2)

			assert(newConnWeight4 === 1)
			assert(expectedMessage.evaluator.auxValue == netval)

		}
	}

}