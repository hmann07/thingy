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


		// INPUTS DO NOT NEED TO BE PART OF THE GENOME

		val inputNeurons = fieldmap.inputs.foldLeft(Map[Int, NeuronGenome]()) { (acc,current) =>
			 acc + (current -> NeuronGenome(
															current, 
															"inputNeuron" + current, 
															0, 
															true, //no hidden must be input
															true,
															Some("SIGMOID"), 
															None, //subnetid
															Weight(), 
															"input"))
			 	}
		val outputNeurons = fieldmap.outputs.foldLeft(Map[Int, NeuronGenome]()) { (acc,current) =>
					acc + (current -> NeuronGenome(current, 
															"outputNeuron" + current, 
															1, 
															true,
															true,
															Some("SIGMOID"), 
															Some(1),  //SubnetId
															Weight(), 
															"output"))
			}


		val (connections, connid) = inputNeurons.foldLeft((Map[Int,ConnectionGenome](), 1)) { (acc, i) =>

			val (itoo, connid2) =  outputNeurons.foldLeft(acc._1, acc._2) { (acc2, o)=> 
				( acc2._1 + (acc2._2 -> ConnectionGenome(acc2._2, i._1, o._1, Weight(), true, true, true, false)), acc2._2 + 1)
			}

			(itoo, connid2)

		}

		// this process should be repeated for each output....
		val subnetNeurons = Map(1 -> NeuronGenome(
									1,
									"subnetIputNeuron1", 
									0, 
									true,
									true,
									Some("SIGMOID"), 
									None, 
									Weight(), 
									"input"),
								2 -> NeuronGenome(
									2,
									"outputNeuron2",
									 1, 
									 true,
									 true,
									 Some("SIGMOID"), 
									 None, 
									 Weight(), 
									 "output"))
		val subnetConnection = Map(1-> ConnectionGenome(1, 1, 2, Weight(), true, true, true, false))

		val subnetwork = NetworkGenome(1, 
						1, //defualt subnet always has just one input and output
						1,
					 subnetNeurons, 
					  subnetConnection, 
					  None, 
					  Some(3))

		NetworkGenome(1, 
			fieldmap.inputs.size,
			fieldmap.outputs.size,
					  inputNeurons ++ outputNeurons, 
					  connections, 
					  Some(Map(1->subnetwork)), 
					  Some(0), 
					  0,
					  0) 


	}



}