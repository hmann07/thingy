package com.lightbend.akka.sample

import akka.testkit.TestFSMRef
import akka.actor.FSM



import org.scalatest._
import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit, TestActorRef, TestProbe }
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory

import com.thingy.weight.Weight
import com.thingy.node._
import com.thingy.neuron.{Predecessor, Successor, Neuron, Initialising, Ready}
import com.thingy.genome.NetworkGenome.NeuronGenome
import com.thingy.genome.NetworkGenomeBuilder

class TestGenome extends FlatSpec {

	val config = ConfigFactory.load()
	val gNet = new NetworkGenomeBuilder()
	val networkGenome = gNet.generateFromSeed

	implicit val system = ActorSystem()

	
	"the genome" should "create a flat representation of its connections" in {
		
		val fg = networkGenome.flattenGenome

		assert(fg == Set("0:1", "0:2", "3:1"))
	}

	"the same genomes" should "have a distance of 1 " in {
		val d = networkGenome.distance(networkGenome)

		assert(d==1)
	}
}