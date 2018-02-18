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


}		

	/** @groupname mutation Mutation */

	/**
	  * Case Class weight has its apply method overridden in the
	  * companion object
	  */

	case class Weight (value: Double = 0.0) {
		import Weight._

		
		val weightRange: Double = config.getConfig("thingy").getDouble("weight-range")
		
		def init: Weight = Weight(Random.nextDouble() * (weightRange - (weightRange / 2))) 

		/** @group mutation */

		/**
		  * Jiggle is a mutation function that will return a new weight with value
		  * which has been jiggled by a certain amount
		  * @todo make it jiggle by some amount in a positive or negative direction. (rather than fixed to 0.1)
		  */

		 def jiggle = Weight(value = value + 0.1)

		 /** @group mutation */

		 /**
		   * reset should will return a new Weight with a newly randomised weight
		   * not based on its previous value.
		   */
		 def reset = Weight().init


		}
