package com.thingy.genome

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import java.io.FileInputStream
import play.api.libs.functional.syntax._
import com.typesafe.config.ConfigFactory

object NetworkGenomeBuilder {

	
	implicit val config = ConfigFactory.load()
	
	val file = config.getConfig("thingy").getString("seed-network")
	val stream = new FileInputStream(file)
	
		
}

class NetworkGenomeBuilder {
	import NetworkGenomeBuilder._
	val json = try {  Json.using[Json.WithDefaultValues].parse(stream) } finally { stream.close() }
}
