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
import com.thingy.genome.ConnectionGenome
import com.thingy.genome.GenomeIO
import com.thingy.genome.NetworkGenomeBuilder
import com.thingy.innovation.Innovation
import com.thingy.config.ConfigDataClass.ConfigData
import com.thingy.environment._


class TestGenome extends FlatSpec {

	val config = ConfigFactory.load()

    val envSpec = EnvironmentIOSpec(List(1,2), List(3))

	val networkGenome = GenomeIO(None, None).generateFromSpec(envSpec)


	implicit val system = ActorSystem()

	
	"the genome" should "create a flat representation of its connections" in {
		
		val fg = networkGenome.flattenGenome

		assert(fg.head._1 == (0,1))
	}

	"the same genomes" should "have a distance of 0 " in {
		val d = networkGenome.distance(networkGenome, ConfigData())

		assert(d==0)
	}

}