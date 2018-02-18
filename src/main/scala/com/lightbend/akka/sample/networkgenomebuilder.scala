package com.thingy.genome

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import java.io.FileInputStream
import play.api.libs.functional.syntax._

object NetworkGenomeBuilder {

	import NetworkGenome._
	implicit val config = ConfigFactory.load()
	
	val file = config.getConfig("thingy").getString("seed-network")
	val stream = new FileInputStream(file)
	val json = try {  Json.using[Json.WithDefaultValues].parse(stream) } finally { stream.close() }
		
	}
