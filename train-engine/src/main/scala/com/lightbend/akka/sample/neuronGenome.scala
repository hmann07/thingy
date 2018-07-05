package com.thingy.genome

import com.thingy.weight.Weight

case class NeuronGenome(
				id: Int, 
				name: String, 
				layer: Double, 
				activationFunction: Option[String] = Some("SIGMOID"), 
				subnetId: Option[Int],
				biasWeight: Weight,
				nodeType: String
			)
