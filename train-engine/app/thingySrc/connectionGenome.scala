package com.thingy.genome

import com.thingy.weight.Weight
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import play.api.libs.functional.syntax._

object ConnectionGenome {

	implicit lazy val connectionReads: Reads[ConnectionGenome] = (
	 (JsPath \ "id").read[Int] and
	 (JsPath  \ "from").read[Int] and
	 (JsPath  \ "to").read[Int] and
	 (JsPath  \ "weight").read[Weight].orElse(Reads.pure(Weight())) and
	  (JsPath  \ "isConnectedInput").read[Boolean] and
	 (JsPath  \ "isConnectedOutput").read[Boolean] and
	 (JsPath  \ "enabled").read[Boolean] and 
	 (JsPath  \ "recurrent").read[Boolean].orElse(Reads.pure(false))
	) (ConnectionGenome.apply _)

	implicit lazy val connectionWrites: Writes[ConnectionGenome] = (
	 (JsPath \ "id").write[Int] and
	 (JsPath  \ "from").write[Int] and
	 (JsPath  \ "to").write[Int] and
	 (JsPath  \ "weight").write[Weight] and
	 (JsPath  \ "isConnectedInput").write[Boolean] and
	 (JsPath  \ "isConnectedOutput").write[Boolean] and
	 (JsPath  \ "enabled").write[Boolean] and 
	 (JsPath  \ "recurrent").write[Boolean]
	) (unlift(ConnectionGenome.unapply))

}

case class ConnectionGenome(
	id: Int, 
	from: Int, 
	to: Int, 
	weight: Weight, 
	isConnectedInput: Boolean,
	isConnectedOutput: Boolean,
	enabled: Boolean = true, 
	recurrent: Boolean = false
)
