package com.lightbend.akka.sample

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
import com.thingy.genome._
import com.thingy.neuron.{Predecessor, Successor, Neuron}


class TestConnection extends FlatSpec {

val config = ConfigFactory.load()

val w = Weight()
implicit val system = ActorSystem()
val nGenome = NeuronGenome(
						1, 
						"testNeuron", 
						0.5, 
						true,
						true,
						Some("SIGMOID"), 
						None,
						Weight(),
						"hidden"
					)
val na = TestFSMRef(new Neuron(nGenome))
val n = Node(1, na)




"The connection weight" should "remain consistent each time it is accessed" in {
	   val c1 = Successor(n, w, false)
	   val a1 = c1.weight.value
	   val a2 = c1.weight.value

	   assert(a1 == a2)
}

"The connection weight" should "should be updated to 2.0" in {
	   val c1 = Successor(n, w, false)
	   val updatedConn = c1.copy(weight =  Weight(2.0))

	   assert(updatedConn.weight.value == 2.0)
}

"The connection weight" should "be jiggled and different, the actor should be the same" in {
	   val c1 = Successor(n, w, false)
	   val updatedConn = c1.copy(weight =  c1.weight.jiggle)

	   assert(updatedConn.weight.value != c1.weight.value && c1.node.actor === updatedConn.node.actor)
}

}
