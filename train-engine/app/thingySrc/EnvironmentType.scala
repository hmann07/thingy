package com.thingy.environment

import play.api.libs.json._
import reactivemongo.play.json._
import reactivemongo.bson.{
  BSONWriter, BSONDocument, BSONDouble, BSONDocumentWriter, BSONDocumentReader, Macros, document
}

trait EnvironmentType {
	val environmentIOSpec: EnvironmentIOSpec
}



class FileEnvironmentType(data: BSONDocument) extends EnvironmentType {

		
		private val jsonEnvData =  Json.toJson(data)
 		private val (inputs, outputs,_) = (jsonEnvData \ "fieldmap").get.as[List[String]].foldLeft((List[Int](), List[Int](), 1)) {(acc, current) =>
 				current match {
 					case "Input" => (acc._3 :: acc._1, acc._2, acc._3 +1)
 					case "Output" =>(acc._1, acc._3 :: acc._2,acc._3 +1)
 				}
 		}

 		val environmentIOSpec = EnvironmentIOSpec(inputs, outputs)
 		val environmentConnetion = (jsonEnvData \ "filename").get.as[String]
}