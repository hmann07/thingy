package com.lightbend.akka.sample

import akka.testkit.TestFSMRef
import akka.actor.FSM

import org.scalatest._
import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit, TestActorRef, TestProbe }
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory

import com.thingy.innovation._
import com.thingy.genome.NetworkGenomeBuilder

class TestInnovation extends FlatSpec {

	val config = ConfigFactory.load()

	implicit val system = ActorSystem()
	val gNet = new NetworkGenomeBuilder()
	val snInnovation = TestFSMRef( new Innovation(gNet))
	
	"THe network schema settings object" should "be populated after startup with the appropriate Map" in {
		val netLayout = Map("1:3" -> 1, "2:3" -> 2)
		val t = snInnovation.stateData.networkConnectionTracker
		assert(t.library == netLayout)
	
	}

	"THe subnet schema settings object" should "be populated after startup with the appropriate Map" in {
		val netLayout = Map("1:2" -> 1)
		val t = snInnovation.stateData.subnetConnectionTracker
		assert(t.library == netLayout)
	
	}

	"The subnets" should "be populated after startup with the appropriate Map" in {
		val subnets = Map(Set(1) -> 1)
		val t = snInnovation.stateData.subnetTracker
		assert(t.library == subnets)
	
	}

	"When an innovation occurs, the innovation id for networks" should "increase to 3" in {
		snInnovation ! Innovation.NetworkConnectionInnovation(3,3)
		assert(snInnovation.stateData.networkConnectionTracker.currentInnovationId == 3)
	}

	"now, when we request the same connection again we" should "have the same id returned again" in {
		snInnovation ! Innovation.NetworkConnectionInnovation(3,3)
		assert(snInnovation.stateData.networkConnectionTracker.currentInnovationId == 3)
	}

	"add another new one and we" should "get another new one" in {
		snInnovation ! Innovation.NetworkConnectionInnovation(2,2)
		assert(snInnovation.stateData.networkConnectionTracker.currentInnovationId == 4)
	}

	"When a subnetwork innovation occurs, the innovation id for subnetworks" should "increase by 1" in {
		val oldid = snInnovation.stateData.subnetConnectionTracker.currentInnovationId
		snInnovation ! Innovation.SubNetConnectionInnovation(2,2)
		assert(snInnovation.stateData.subnetConnectionTracker.currentInnovationId == oldid + 1)
	}

	"Just for comlpeteness when requested again we" should "get the same number" in {
		val oldid = snInnovation.stateData.subnetConnectionTracker.currentInnovationId
		snInnovation ! Innovation.SubNetConnectionInnovation(2,2)
		assert(snInnovation.stateData.subnetConnectionTracker.currentInnovationId == oldid)
	}
}