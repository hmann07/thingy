package com.thingy.genome



case class NeuronGenome(
				id: Int, 
				name: String, 
				layer: Double, 
				activationFunction: Option[String] = Some("SIGMOID"), 
				subnetId: Option[Int]
			)
