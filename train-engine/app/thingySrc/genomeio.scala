package com.thingy.genome

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import play.api.libs.json.JsSuccess
import java.io.FileInputStream
import play.api.libs.functional.syntax._

import com.thingy.genome._
import com.thingy.weight.Weight
//import com.thingy.genome.ConnectionGenome
//import com.thingy.genome.NetworkGenome


case class GenomeIO(json: Option[JsValue], genome: Option[()=>NetworkGenome]) {

	implicit def weightReads = new Reads[Weight] {
  		def reads(js: JsValue): JsResult[Weight] = js match {
  					case JsNumber(d) => JsSuccess(Weight(d.toDouble))
  					case _ => JsSuccess(Weight())
  				}
    }


	implicit val neuronReads: Reads[NeuronGenome] = (
	 (JsPath \ "id").read[Int] and
	 (JsPath  \ "name").read[String] and
	 (JsPath  \ "layer").read[Double] and
	 (JsPath  \ "activationFunction").readNullable[String] and
	 (JsPath  \ "subnetid").readNullable[Int] and
	 (JsPath  \ "biasWeight").read[Weight].orElse(Reads.pure(Weight())) and 
	 (JsPath  \ "type").read[String]

	) (NeuronGenome.apply _)



	


	implicit lazy val connectionReads: Reads[ConnectionGenome] = (
	 (JsPath \ "id").read[Int] and
	 (JsPath  \ "from").read[Int] and
	 (JsPath  \ "to").read[Int] and
	 (JsPath  \ "weight").read[Weight].orElse(Reads.pure(Weight())) and
	 (JsPath  \ "enabled").read[Boolean] and 
	 (JsPath  \ "recurrent").read[Boolean].orElse(Reads.pure(false))
	) (ConnectionGenome.apply _)



	implicit lazy val networkReads: Reads[NetworkGenome] = (
	 (JsPath \ "id").read[Int] and
	 (JsPath \ "neurons").read[Seq[NeuronGenome]].map{_.foldLeft(Map[Int, NeuronGenome]()){(acc, current) => acc + (current.id -> current)}} and
	 (JsPath \ "connections").read[Seq[ConnectionGenome]].map{_.foldLeft(Map[Int, ConnectionGenome]()){(acc, current) => acc + (current.id -> current)}} and
	 (JsPath \ "subnets").lazyReadNullable[Seq[NetworkGenome]](Reads.seq(networkReads)).map{_.map{_.foldLeft(Map[Int, NetworkGenome]()){(acc, current) => acc + (current.id -> current)}}} and
	 (JsPath  \ "parentId").readNullable[Int] and
	 (JsPath  \ "species").read[Int].orElse(Reads.pure(0)) and
	 (JsPath  \ "generation").read[Int].orElse(Reads.pure(0))
	) (NetworkGenome.apply _)

	//log.debug("loaded json file {}", json)
	def generate: NetworkGenome = {
			genome.map(g => g()).getOrElse({
					generateFromSeed
				}).asInstanceOf[NetworkGenome]
	}	

	def generateFromSeed: NetworkGenome = {

		json.map(j => {

			j.validate[NetworkGenome] match {

		  case g: JsSuccess[NetworkGenome] => {
				val genome = g.get
				genome
			}}
		}).get
	}

}