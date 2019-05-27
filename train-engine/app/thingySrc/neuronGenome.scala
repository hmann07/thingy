package com.thingy.genome

import com.thingy.weight.Weight
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import play.api.libs.functional.syntax._

object NeuronGenome {

	implicit val neuronReads: Reads[NeuronGenome] = (
	 (JsPath \ "id").read[Int] and
	 (JsPath  \ "name").read[String] and
	 (JsPath  \ "layer").read[Double] and
	 (JsPath  \ "isInputConnected").read[Boolean] and
	 (JsPath  \ "isOutputConnected").read[Boolean] and
	 (JsPath  \ "activationFunction").readNullable[String] and
	 (JsPath  \ "subnetid").readNullable[Int] and
	 (JsPath  \ "biasWeight").read[Weight].orElse(Reads.pure(Weight())) and 
	 (JsPath  \ "type").read[String]

	) (NeuronGenome.apply _)

	implicit val neuronWrites: Writes[NeuronGenome] = (
	 (JsPath \ "id").write[Int] and
	 (JsPath  \ "name").write[String] and
	 (JsPath  \ "layer").write[Double] and
	  (JsPath  \ "isInputConnected").write[Boolean] and
	 (JsPath  \ "isOutputConnected").write[Boolean] and
	 (JsPath  \ "activationFunction").writeNullable[String] and
	 (JsPath  \ "subnetid").writeNullable[Int] and
	 (JsPath  \ "biasWeight").write[Weight] and
	 (JsPath  \ "type").write[String]
	) (unlift(NeuronGenome.unapply))

}


case class NeuronGenome(
				id: Int, 
				name: String, 
				layer: Double, 
				isInputConnected: Boolean,
				isOutputConnected: Boolean,
				activationFunction: Option[String] = Some("SIGMOID"), 
				subnetId: Option[Int],
				biasWeight: Weight,
				nodeType: String
			)
