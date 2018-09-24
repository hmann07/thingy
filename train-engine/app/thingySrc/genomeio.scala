package com.thingy.genome

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import play.api.libs.json.JsSuccess
import java.io.FileInputStream
import play.api.libs.functional.syntax._

import com.thingy.genome._
import com.thingy.weight.Weight
import com.thingy.environment.EnvironmentIOSpec

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


	// Build the most basic seed network based on input/ouput spec. All outputs will automatically be given a neutral subnet:
	// one input, one ouput one connection with weight 1. ,
	// this should receive a tuple of two lists (1,2,5),(3,5)
	def generateFromSpec(fieldmap: EnvironmentIOSpec): NetworkGenome = {

		val inputNeurons = fieldmap.inputs.foldLeft(Map[Int, NeuronGenome]()) { (acc,current) =>
			 acc + (current -> NeuronGenome(
															current, 
															"inputNeuron" + current, 
															0, 
															Some("SIGMOID"), 
															None, //subnetid
															Weight(), 
															"input"))
			 	}
		val outputNeurons = fieldmap.outputs.foldLeft(Map[Int, NeuronGenome]()) { (acc,current) =>
					acc + (current -> NeuronGenome(current, 
															"outputNeuron" + current, 
															1, 
															Some("SIGMOID"), 
															Some(1),  //SubnetId
															Weight(), 
															"output"))
			}


		val (connections, connid) = inputNeurons.foldLeft((Map[Int,ConnectionGenome](), 1)) { (acc, i) =>

			val (itoo, connid2) =  outputNeurons.foldLeft(acc._1, acc._2) { (acc2, o)=> 
				( acc2._1 + (acc2._2 -> ConnectionGenome(acc2._2, i._1, o._1, Weight(), true, false)), acc2._2 + 1)
			}

			(itoo, connid2)

		}

		// this process should be repeated for each output....
		val subnetNeurons = Map(1 -> NeuronGenome(1,"subnetIputNeuron1", 0, Some("SIGMOID"), None, Weight(), "input"),
								2 -> NeuronGenome(2,"outputNeuron2", 1, Some("SIGMOID"), None, Weight(), "output"))
		val subnetConnection = Map(1-> ConnectionGenome(1, 1, 2, Weight(), true, false))

		val subnetwork = NetworkGenome(1, 
					 subnetNeurons, 
					  subnetConnection, 
					  None, 
					  Some(3))

		NetworkGenome(1, 
					  inputNeurons ++ outputNeurons, 
					  connections, 
					  Some(Map(1->subnetwork)), 
					  Some(0), 
					  0,
					  0) 


	}



}