package com.thingy.weight

import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, FSM }

/**
  * Weight object is designed to encapsulate functionality related
  * to the weights, as well as defining the weight it self methods
  * allow control over the weight ranges and also specific mutations
  * related to it.
  */
object Weight {

	implicit val config = ConfigFactory.load()

	val weightRange: Double = config.getConfig("thingy").getDouble("weight-range")

	def rGen = scala.util.Random


	/**
	  * nextValue should return a new randomised Double.
	  * It should sit between the range specified via config centred on zero.
	  */

	def nextValue = (weightRange / 2) - (rGen.nextDouble() *  weightRange)


	/**
	  * setDefault is used by the weight case class to create a random weight when none has been specified.
	  * @return A double to be used as a connection weight.
	  */

	def setDefault(): Double = {
		nextValue
	}

	def apply(): Weight = Weight(setDefault)

}

	/** @groupname mutation Mutation */

	/**
	  * Case Class weight has its apply method overridden in the
	  * companion object
	  */

	case class Weight (value: Double) {
		import Weight._
		val jigres = config.getConfig("thingy").getDouble("jiggle-over-reset")
		/** @group mutation */

		/** Mutate, with some probability, jiggle the weight or, reset it completely
		 *
		 */
		 def mutate = if(Random.nextDouble < jigres) jiggle else reset

		/**
		  * Jiggle is a mutation function that will return a new weight with value
		  * which has been jiggled by a certain amount
		  * @todo make it jiggle by some amount in a positive or negative direction. (rather than fixed to 0.1)
		  */

		 def jiggle = Weight(value = value + {if(Random.nextDouble<0.5)-1 else 1} * 0.05)

		 /** @group mutation */

		 /**
		   * reset should will return a new Weight with a newly randomised weight
		   * not based on its previous value.
		   */
		 def reset = Weight()


		}