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
import com.thingy.config.ConfigDataClass.{ConfigData, RuntimeConfig}
import com.thingy.environment._
import com.thingy.environment.EnvironmentType
import com.thingy.evaluator.SSEEvaluator
import com.thingy.population.{Population, Active}
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json._
import play.modules.reactivemongo.json.collection._
import com.thingy.config.ConfigDataClass.ConfigData




class TestPopulation extends TestKit(ActorSystem("MySpec")) with ImplicitSender  with Matchers with FlatSpecLike with BeforeAndAfterAll  {
	

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
	val cd = ConfigData(populationSize = 2, maxGenerations = 1)
	
	val bd = BSONDocument(
		"_id"->"5b82acaf45f7766ca6ab9f8c",
		"name"->"xor",
		"fieldmap"-> List("Input","Input","Output"),
		"filename"->"Xor.json")


	
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


	val outflow = TestProbe()

	val population = TestFSMRef(new Population(outflow.ref, cd, RuntimeConfig(mongoStorage = false))
								, "pop1")


	"population" should "go to active" in {
		assert(population.stateName == Active)
	}



	
}