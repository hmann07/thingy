package com.thingy.genome

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import java.io.FileInputStream
import play.api.libs.functional.syntax._

import com.thingy.genome._
import com.thingy.weight.Weight
//import com.thingy.genome.ConnectionGenome
//import com.thingy.genome.NetworkGenome


case class GenomeIO(json: Option[JsValue], genome: Option[()=>NetworkGenome]) {


	implicit val neuronReads: Reads[NeuronGenome] = (
	 (JsPath \ "id").read[Int] and
	 (JsPath  \ "name").read[String] and
	 (JsPath  \ "layer").read[Double] and
	 (JsPath  \ "activationFunction").readNullable[String] and
	 (JsPath  \ "subnetid").readNullable[Int]

	) (NeuronGenome.apply _)



	implicit def connectionReads: Reads[ConnectionGenome] = (
	 (JsPath \ "id").read[Int] and
	 (JsPath  \ "from").read[Int] and
	 (JsPath  \ "to").read[Int] and
	 (JsPath  \ "weight").read[Weight].orElse(Reads.pure(Weight().init)) and
	 (JsPath  \ "enabled").read[Boolean] and 
	 (JsPath  \ "recurrent").read[Boolean].orElse(Reads.pure(false))
	) (ConnectionGenome.apply _)



	implicit lazy val networkReads: Reads[NetworkGenome] = (
	 (JsPath \ "id").read[Int] and
	 (JsPath \ "neurons").read[Seq[NeuronGenome]].map{_.foldLeft(Map[Int, NeuronGenome]()){(acc, current) => acc + (current.id -> current)}} and
	 (JsPath \ "connections").read[Seq[ConnectionGenome]].map{_.foldLeft(Map[Int, ConnectionGenome]()){(acc, current) => acc + (current.id -> current)}} and
	 (JsPath \ "subnets").lazyReadNullable[Seq[NetworkGenome]](Reads.seq(networkReads)).map{_.map{_.foldLeft(Map[Int, NetworkGenome]()){(acc, current) => acc + (current.id -> current)}}} and
	 (JsPath  \ "parentId").readNullable[Int]
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
		})
	}

}