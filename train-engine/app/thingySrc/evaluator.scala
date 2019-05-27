package com.thingy.evaluator

import com.thingy.environment.Environment.Representation
import com.thingy.environment.EnvironmentIOSpec

trait Evaluator {
	val epochLength: Int
	val fitness: Double
	val auxValue: Double
	def evaluateIteration(networkInput: Representation, networkOutput: Map[Int, Double]): Evaluator
	def evaluateEpoch() : Evaluator
	def reset: Evaluator
	
}
case class SSEEvaluator(
	val epochLength: Int = 0,
	val aggregatedIterationValue: Double = 0.0,
	val fitness: Double = 0.0,
	val auxValue: Double = 0.0,
	val fieldMap: EnvironmentIOSpec = EnvironmentIOSpec()) extends Evaluator {
	
	// Will be evaluated after each pattern.So will test the response to a particular representation.
	override def evaluateIteration(networkInput: Representation, networkOutput: Map[Int, Double]): Evaluator = {
		  

		  // this needs to look through the fieldmap...? or a better rep? it's important that the right output is checked against the
		  // correct expected output.

		 val sumSquaredError =  fieldMap.outputs.foldLeft(0.0) { (acc, current) =>
			val error = networkInput.expectedOutput(current) - networkOutput(current)
  		  	val squaredError = math.pow(error, 2)		  		

  		  	acc + squaredError

		  }

  		 
          copy(aggregatedIterationValue = aggregatedIterationValue + sumSquaredError)
	}


	// Will be evaluated after the whole pattern has been consumed. 
	override def evaluateEpoch(): Evaluator = {
		
		val evalfitness = 1.0 / aggregatedIterationValue
		
		copy(fitness = evalfitness, auxValue = aggregatedIterationValue)
	}

	override def reset: Evaluator = {
		copy(aggregatedIterationValue = 0.0, fitness = 0.0, auxValue = 0.0)
	}

} 

