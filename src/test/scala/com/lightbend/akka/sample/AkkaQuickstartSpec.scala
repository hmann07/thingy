//#full-example
package com.lightbend.akka.sample

import org.scalatest._
import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit, TestActorRef, TestProbe }
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory

import com.thingy.weight.Weight


class TestWeight extends FlatSpec {

val config = ConfigFactory.load()

val w = Weight().init

"A weight" should "have a value that's a double" in {
        assert(w.value > 0)
  }

 "The weight" should "not be greater or less than specified boundary" in {
 		val boundary = config.getConfig("thingy").getDouble("weight-range")
 		assert(w.value < boundary && w.value > -boundary)
 }

 "The weight" should "remain consistent each time it is accessed" in {
		val wv = w.value
 		assert(wv == w.value)
 }

 "When two instances of weight exist the values" should "remain independent" in {
 		val w2 = Weight(w.value + 0.2)
		assert(w2.value != w.value)
 }

 "When two instances of weight exist the values" should "remain independent when the random values are automatically generated" in {
 		val w2 = Weight().init
		assert(w2.value != w.value)
 }
/*
 "jiggling the weight" should "change value by 0.1" in {
	   val w2 = Weight(w.value()).jiggle
 	   assert(w2.value() == w.value() + 0.1)
 }
*/

 "manually setting the weight to 3" should "set the weight to 3" in {
 	  val w2 = Weight(3.0)
 	  assert(w2.value == 3.0)
 }

}
