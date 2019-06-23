package com.thingy.evaluator

import com.thingy.environment.Environment.Representation
import com.thingy.environment.EnvironmentIOSpec

case class SoftmaxEvaluator(
	val epochLength: Int = 0,
	val aggregatedIterationValue: Double = 0.0,
	val fitness: Double = 0.0,
	val auxValue: Double = 0.0,
	val fieldMap: EnvironmentIOSpec = EnvironmentIOSpec()) extends Evaluator {
	
	override def evaluateIteration(networkInput: Representation, networkOutput: Map[Int, Double]): Evaluator = {
		// go through each output
		// compare to expected output 
			//  softmax = e^output(i) / sum(e^ouput)
		//println(networkInput + " ----- " + networkOutput)
		// first cal all expos we need the sum, but then then calc each individually.. may aswell only expo once...
		// becuase this calcualtes the proportion of total need to do this first, then go back through to work out cross entropy
		val (expos, total) = networkOutput.foldLeft((List[(Int, Double)](), 0.0) ) { case ((expos,agg), (outputIdx, outputValue)) => 
			val expo: Double = Math.exp(outputValue)
			( (outputIdx, expo) +: expos, agg + expo)
		}


		// Cross entropy loss function = H(y,p)=−∑ yi log(pi) where yi = trainign labels anf pi = probabilty from softmax
		// go through each input and match to the output. then take input * log output

		//val ps = expos.map(x=> x._2 / total)
		//println("probs are: " + ps)
			
		val cross_entropy_loss= expos.foldLeft(0.0){ (cetotal, expo)=>
		 //println("exponentvalue: " + (expo._2 / total) + " expected value " + networkInput.expectedOutput(expo._1))

		 val cel = networkInput.expectedOutput(expo._1) * Math.log(expo._2 / total)
		 //println("ouput  created: " + networkInput.expectedOutput(expo._1) + " log of prob val: " + Math.log(expo._2 / total)  + " cel is: " + cel) 
		 cel + cetotal
		} * -1

		//println("cross entropy was : " + cross_entropy_loss)
		copy(aggregatedIterationValue = cross_entropy_loss + aggregatedIterationValue , epochLength = epochLength + 1)

	}

	override def evaluateEpoch() = {
		//provide the final performance
		// average of the cross entropy across samples

		val finalLoss = aggregatedIterationValue / epochLength

		copy(auxValue = finalLoss, fitness=1/finalLoss)

	}

	override def reset = {
		copy(epochLength = 0, aggregatedIterationValue = 0.0, auxValue = 0.0, fitness = 0.0 )
	}
} 