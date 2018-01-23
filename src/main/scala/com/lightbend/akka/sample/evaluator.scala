package com.thingy.evaluator

import com.thingy.environment.Environment.Representation


trait Evaluator {
	val epochLength: Int
	val fitness: Double
	val auxValue: Double
	def evaluateIteration(networkInput: Representation, networkOutput: Map[Int, Double]): Evaluator
	def evaluateEpoch() : Evaluator
	def reset: Evaluator
	
}
case class XOREvaluator(
	val epochLength: Int = 0,
	val aggregatedIterationValue: Double = 0,
	val fitness: Double = 0,
	val auxValue: Double = 0) extends Evaluator {
	
	override def evaluateIteration(networkInput: Representation, networkOutput: Map[Int, Double]): Evaluator = {
		  
  		  val error = networkInput.expectedOutput(3) - networkOutput(3)
  		  val squaredError = math.pow(error, 2)
          
          this.copy(aggregatedIterationValue = aggregatedIterationValue + squaredError)
	}


	override def evaluateEpoch() = {
		
		val evalfitness = 1 / aggregatedIterationValue
		
		this.copy(fitness = evalfitness, auxValue = aggregatedIterationValue)
	}

	override def reset = {
		this.copy(aggregatedIterationValue = 0, fitness = 0, auxValue = 0)
	}

} 

