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

class TestNeuron extends FlatSpec {

	val config = ConfigFactory.load()

	val w = Weight()

	implicit val system = ActorSystem()

	val na = TestFSMRef(new Neuron)
	val n = Node(1, na)

	"the neuron" should "be in state Initialising when started" in {
		assert(na.stateName == Initialising)
	}

	"the neuron" should "then move to state Ready, when provided with sufficient connection detail" in {
		val ns = Neuron.ConnectionConfig(outputs = List(Successor(n, Weight()))) // a connection to itself.
		n.actor ! ns
		assert(na.stateName == Ready)
	}
}