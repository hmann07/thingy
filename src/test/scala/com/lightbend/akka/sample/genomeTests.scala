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
import com.thingy.genome.NeuronGenome
import com.thingy.genome.ConnectionGenome
import com.thingy.genome.GenomeIO
import com.thingy.genome.NetworkGenomeBuilder
import com.thingy.innovation.Innovation

class TestGenome extends FlatSpec {

	val config = ConfigFactory.load()
	val nb = new NetworkGenomeBuilder()
	val networkGenome = new GenomeIO(Some(nb.json), None).generateFromSeed

	val networkupdate = Innovation.NetworkNeuronInnovationConfirmation(networkGenome.connections.head._2, 4, 4, 5)
	val newG = networkGenome.updateNetworkGenome(networkupdate)

	implicit val system = ActorSystem()

	
	"the genome" should "create a flat representation of its connections" in {
		
		val fg = networkGenome.flattenGenome

		assert(fg.head._1 == (0,1))
	}

	"the same genomes" should "have a distance of 0 " in {
		val d = networkGenome.distance(networkGenome)

		assert(d==0)
	}

	"mutated genomes" should "have longer connection list" in {
		
		assert(newG.connections.size == 4)
		
	}
	

	"different genomes" should "have a distance > 0.0 " in {
		val d = networkGenome.distance(newG)
		
		assert(d>0)
	}
}