package com.thingy.genome

import com.typesafe.config.ConfigFactory
import play.api.libs.json._
import java.io.FileInputStream
import play.api.libs.functional.syntax._
import akka.actor.{ ActorRef, FSM, Props, ActorContext }
import com.thingy.neuron.Neuron
import com.thingy.network._
import com.thingy.subnetwork._
import com.thingy.neuron.{Successor, Predecessor}
import com.thingy.weight.Weight
import com.thingy.node._
import com.thingy.innovation.Innovation
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import java.io.FileInputStream
import play.api.libs.functional.syntax._
import scala.util.Random
import com.thingy.config.ConfigDataClass.ConfigData
import reactivemongo.bson.{
  BSONWriter, BSONDocument, BSONDouble, BSONDocumentWriter, BSONDocumentReader, Macros, document
}



object NetworkGenome {



	implicit lazy val networkReads: Reads[NetworkGenome] = (
	 (JsPath \ "id").read[Int] and
	 (JsPath \ "inputCount").read[Int] and
	 (JsPath \ "outputCount").read[Int] and
	 (JsPath \ "neurons").read[Seq[NeuronGenome]].map{_.foldLeft(Map[Int, NeuronGenome]()){(acc, current) => acc + (current.id -> current)}} and
	 (JsPath \ "connections").read[Seq[ConnectionGenome]].map{_.foldLeft(Map[Int, ConnectionGenome]()){(acc, current) => acc + (current.id -> current)}} and
	 (JsPath \ "subnets").lazyReadNullable[Seq[NetworkGenome]](Reads.seq(networkReads)).map{_.map{_.foldLeft(Map[Int, NetworkGenome]()){(acc, current) => acc + (current.id -> current)}}} and
	 (JsPath  \ "parentId").readNullable[Int] and
	 (JsPath  \ "species").read[Int].orElse(Reads.pure(0)) and
	 (JsPath  \ "generation").read[Int].orElse(Reads.pure(0))
	) (NetworkGenome.apply _)

	implicit val netWrites: Writes[NetworkGenome] = new Writes[NetworkGenome] {
	    def writes(net: NetworkGenome): JsValue = Json.obj(
	    	"id" -> net.id,
	    	"inputCount" -> net.inputCount,
	    	"outputCount" -> net.outputCount,
	    	"neurons" -> net.neurons.values.map(n => Json.toJson(n)), 
	    	"connections" -> net.connections.values.map(c => Json.toJson(c)), 
	    	"subnets" -> net.subnets.map(os=> os.values.map(s => Json.toJson(s))),
	    	"parent" -> net.parentId,
	    	"species" -> net.species,
	    	"generation" -> net.generation
	        //bar.key -> Json.obj("value" -> bar.value)
	    )
	}

	implicit object genomeWriter extends BSONDocumentWriter[NetworkGenome] {
  		def write(net: NetworkGenome): BSONDocument =
    		BSONDocument(
    				"id" -> net.id,
    				"connections" -> net.connections.values, 
    				"neurons" -> net.neurons.values,
    				"subnets" -> net.subnets.map(slist=>slist.values).getOrElse(List.empty),
    				"parent" -> net.parentId,
	    			"species" -> net.species,
	    			"generation" -> net.generation)
	}

	
}


// GENOMES DO NOT NEED INPUTS...
// OR EVEN OUTPUTS? 
// JUST A SPEC OF HIDDEN NEURONS (WHICH IN SOME CASES WILL BE NULL AND WILL REQUIRE A )

case class NetworkGenome(
				id: Int, 
				inputCount: Int,
				outputCount: Int,
				neurons: Map[Int, NeuronGenome], 
				connections: Map[Int, ConnectionGenome], 
				subnets: Option[Map[Int, NetworkGenome]], 
				parentId: Option[Int], 
				species: Int = 0, 
				generation: Int = 0) {
	
	import NetworkGenome._
	 def innovationHash: Set[Int] = {
	 	connections.values.map(_.id).toSet
	 }

	 def toJson = {
	 	Json.toJson(this)
	 }


	 def flattenGenome: Map[(Int, Int), Double] = {
	 	val connList: Map[(Int, Int), Double] = connections.foldLeft(Map[(Int, Int), Double]() ){(acc, current) => if(current._2.enabled) acc + ((parentId.get,current._1) -> current._2.weight.value) else acc}
	 	val subnetConnList: Map[(Int, Int), Double] = subnets.map(_.foldLeft(Map[(Int, Int), Double]()){(acc, current)=>(acc ++ current._2.flattenGenome)}).getOrElse(Map.empty)
	 	connList ++ subnetConnList
	 }

	 /*
	  * Distance function is primarily to aid speciation. genomes. A measure of how similar or different two genomes are.
	  * closer together will more likely end up in a species together. The calculation is Joined Conns + Disjoint Conns + weightsDiff
	  * If the have genes in common, they are more similar. The more genomes not in common the less similar they are.
	  * The process will be:
	  * Iterate through the genome. 
	  */

	 def distance(genome: NetworkGenome, configData: ConfigData) = {
	 	val g1 = flattenGenome
	 	val g2 = genome.flattenGenome

	 	val (biggest, smallest) = {if (g1.size > g2.size) (g1,g2) else (g2,g1)}
	 	

	 	// whats the count of the union.
	 	//val union = (g1 | g2)
	 	
	 	val (union, intersection, weightDiff, compliment): (Map[(Int, Int), Double],Map[(Int, Int), Double], Double, Map[(Int, Int), Double]) = smallest.foldLeft((biggest,Map[(Int, Int), Double](),0.0, biggest)) { (acc, current) =>
	 		val (inter, wd): (Map[(Int, Int), Double], Double) = biggest.get(current._1).map(matchedG => (acc._2 + current, acc._3 + math.abs(matchedG - current._2))).getOrElse((acc._2, acc._3)) 
	 		val uni = acc._1 + current
	 		val com = acc._4.get(current._1).map(matchedG => acc._4 - current._1).getOrElse(acc._4 + current)
	 		(uni, inter, wd, com)
	 	}

	 	val unionSize = union.size
	 	val intersectionSize = intersection.size

	 	// find the Disjoint

	 	//val disjoint = union.diff(intersection)

	 	//val averageWeightDiff = intersection.foldLeft(0.0)((r,c) => r + math.abs(connections(c).weight - genome.connections(c).weight).toDouble ))

	 	val pctSimilar = ((compliment.size.toDouble * configData.compatDisjointCoeff)  / biggest.size.toDouble) + ((weightDiff / intersectionSize.toDouble) * configData.compatWeightCoeff)

	 	//biggest.size.toDouble
	 	pctSimilar

	 	// find the excess


	 


	 	// find the common to both but calculate variance in weights.


	 	// calculate the distance 


	 }

	 def crossover(partner: NetworkGenome): NetworkGenome = {
	 	// first take all repective connections.
	 	// perhaps make the assumption that this genome is the strongest:
	 	// this has implications:
	 	//	1) will keep all own connections.
	 	// by lineing them up.

	 	val newConnections = connections.foldLeft(Map[Int, ConnectionGenome]()){ (acc, current) =>
	 		val conn: ConnectionGenome = partner.connections.get(current._1).map { c: ConnectionGenome => 
	 				// then return one or other of the connections (i.e. from one or other of the genomes) with some probablity
	 				// one of he connections could be disabled we will enable with some P
	 				// there's potetntial for completely destroying networks with un-enabled connections
	 				// therefore enable all.

	 				// select one or other
					val newConn: ConnectionGenome = {if(Random.nextDouble < 0.5) c else current._2}
	 				 				

	 				if((!c.enabled || !current._2.enabled) && Random.nextDouble < 0.75) {
	 					newConn.copy(enabled = true)
	 				} else {
	 					// we could leave this disabled (if it were) but network may die...
	 					newConn.copy(enabled = true)
	 				}
	 			
	 		}.getOrElse(current._2)
	 		acc + (conn.id -> conn)
	 	}

	 	// now just grab the new neurons.
	 	// perhaps it's best at this point to cross over any that were subnets, so that we get a crossed version of the "neuron".
	 	// automatically pick from fittest parent.
 
	 	val newNeurons = newConnections.foldLeft(Map[Int, NeuronGenome]()) { (acc, current) =>
	 		
	 		val nFrom = neurons(current._2.from)
	 		val nTo = neurons(current._2.to)
	 		acc + (current._2.from -> nFrom  , current._2.to -> nTo)
	 	}


	 	// now update the subnets... go thorugh new neurons if it's a subnet cross over the subnet.. iff 
	 	// it was a matched neuron...
	 	val updatedSubnets = newNeurons.foldLeft(Map[Int, NetworkGenome]()) { (acc, current) =>

	 		val n = current._2
	 		n.subnetId.map(s => 
	 			// neuron has a subnet..
	 			if(partner.neurons.contains(n.id)){
	 				// then there is an opportunity to cross over too..
	 				// this assume that we will not track subnet structures...
	 				// In theory we should ask the innovation to re-set the innovation id of the subnet.  
	 				// In reality this insnt being used anywhere at the moment, so why worry. also if the net goes through any mutations
	 				// then the changes will be tracked. 
	 				// crossover of subnets will not create new neurons or connections which are the important ones that need to be tracked
	 				// IN FACT, does the sructure even change? Possibly not. the dominant genome will dictate the neruons in use
	 				// it won't gain any from the less dominant just a differnet set of weights.. of different bias / activation fn.
	 				val crossednet = subnets.get(s).crossover(partner.subnets.get(s))
	 				acc + (s -> crossednet)
	 			} else {
	 				// no crossover opp
	 				acc
	 			}
	 		) getOrElse acc

	 	}

	 	// TODO: do we need to clearup things neurons that no longer exist. assume the jvm will garb

	 	this.copy(connections = newConnections, neurons = newNeurons, subnets = Some(updatedSubnets))

	 }

	 /*
	  * This function will update the genome by adding a new connection based on
	  * the innovation confirmation which will include the correct id
	  * and the relevant from and to nodes.
	  */
	 def updateNetworkGenome(s: Innovation.InnovationConfirmation): NetworkGenome  = {
	 	val recurrent = if(neurons(s.from).layer < neurons(s.to).layer) {false} else {true}
	 	val newConnection = (s.id -> ConnectionGenome(s.id, s.from, s.to, Weight(), true, recurrent))
	 	this.copy(connections =  connections + newConnection )
	 }


     /*
	  * This updateNetworkGenome function will update the genome by adding a new new node based on
	  * the innovation confirmation. The layer id will need to be computed based on the incoming and outoging nodes
	  * Two new connections will be created and the original connection disabled.
	  */
	 def updateNetworkGenome(s: Innovation.NetworkNeuronInnovationConfirmation): NetworkGenome = {
	 	val neuronLayer = (neurons(s.connectionToBeSplit.from).layer + neurons(s.connectionToBeSplit.to).layer) / 2
	 	
	 	val newNeuron = (s.nodeid -> NeuronGenome(id = s.nodeid, 
	 											  name = "newNeuron" + s.nodeid, 
	 											  layer = neuronLayer, 
	 											  isOutputConnected = s.connectionToBeSplit.isConnectedOutput,
	 											  isInputConnected = s.connectionToBeSplit.isConnectedInput,
	 											  activationFunction = Some("SIGMOID"),
	 											  subnetId = None, 
	 											  biasWeight = Weight(), 
	 											  nodeType = "hidden"))
	 	


	 	val recurrentPrior = if(neurons(s.connectionToBeSplit.from).layer < neuronLayer) {false} else {true}
	 	// use weight from old connection
	 	val newPrior = (s.priorconnectionId -> ConnectionGenome(
	 												s.priorconnectionId, 
	 												s.connectionToBeSplit.from, 
	 												s.nodeid, 
	 												s.connectionToBeSplit.weight,
	 												s.connectionToBeSplit.isConnectedInput, // if old connection is input conected the new prior will be too. 
	 												false, // new neuron t.f. can't be connected to output
	 												true, 
	 												recurrentPrior

	 											))
	 	val recurrentPost = if(neuronLayer < neurons(s.connectionToBeSplit.to).layer) {false} else {true}
	 	// set the weight to 1 to minimise disruption
	 	val newPost = (s.postconnectionId -> ConnectionGenome(
	 											s.postconnectionId, 
	 											s.nodeid, 
	 											s.connectionToBeSplit.to, 
	 											Weight(1), 
	 											false, // new neuron, won't be input 
	 											s.connectionToBeSplit.isConnectedOutput, // if old conn was connected output this connection will be too
	 											true, 
	 											recurrentPost
	 										))
	 	val updatedConnectionList = connections + (s.connectionToBeSplit.id -> connections(s.connectionToBeSplit.id).copy(enabled = false))
	 		 	
	 	this.copy(connections = updatedConnectionList + newPrior + newPost, neurons =  neurons + newNeuron  )

	 }

	 def updateSubnet(s: Innovation.SubnetConnectionInnovationConfirmation) = {
	 	val updatedSubnets = subnets.get.foldLeft(Map[Int, NetworkGenome]()) { (subnets, current) =>
	 		val currentObj = current._2
	 		val updatedNet = if(currentObj.id == s.originalRequest.existingNetId) {
	 			val recurrent = if(currentObj.neurons(s.originalRequest.from).layer < currentObj.neurons(s.originalRequest.to).layer) {false} else {true}
	 			val newConnection = (s.updatedConnectionTracker -> ConnectionGenome(s.updatedConnectionTracker, s.originalRequest.from, s.originalRequest.to, Weight(), true, recurrent))
	 			(s.updatedNetTracker ->  currentObj.copy(id = s.updatedNetTracker, connections = currentObj.connections + newConnection))
	 		} else {
	 			current
	 		}
	 		subnets + updatedNet
	 	}

	 	val updatedNeuron = neurons.foldLeft(Map[Int, NeuronGenome]()) { (neurons, current) =>
	 			val currentObj = current._2
	 			val updatedNeuron = (currentObj.id -> {if(currentObj.id == s.originalRequest.neuronId) {
	 				currentObj.copy(subnetId = Some(s.updatedNetTracker))
	 			} else {
	 				currentObj
	 			}})
	 		neurons + updatedNeuron
	 	}

	 	this.copy(neurons = updatedNeuron, subnets = Some(updatedSubnets))
	}

	def updateSubnet(s: Innovation.SubNetNeuronInnovationConfirmation): NetworkGenome = {
		subnets.map(subnetList => {
		
			val subnet = subnetList(s.originalRequest.existingNetId)
			val neuronLayer = (subnet.neurons(s.connectionToBeSplit.from).layer + subnet.neurons(s.connectionToBeSplit.to).layer) / 2
	 		
	 		val newNeuron = (s.nodeid -> NeuronGenome(id = s.nodeid, 
	 											  name = "newNeuron" + s.nodeid, 
	 											  layer = neuronLayer, 
	 											  isOutputConnected = s.connectionToBeSplit.isConnectedOutput,
	 											  isInputConnected = s.connectionToBeSplit.isConnectedInput,
	 											  activationFunction = Some("SIGMOID"),
	 											  subnetId = None, 
	 											  biasWeight = Weight(), 
	 											  nodeType = "hidden"))
	 		
	 		val recurrentPrior = if(subnet.neurons(s.connectionToBeSplit.from).layer < neuronLayer) {false} else {true}
	 		// use old weight
	 		val newPrior = (s.priorconnectionId -> ConnectionGenome(s.priorconnectionId, s.connectionToBeSplit.from, s.nodeid, s.connectionToBeSplit.weight, true, recurrentPrior))
	 		val recurrentPost = if(neuronLayer < subnet.neurons(s.connectionToBeSplit.to).layer) {false} else {true}
	 		// use 1 to minimise disruption
	 		val newPost = (s.postconnectionId -> ConnectionGenome(s.postconnectionId, s.nodeid, s.connectionToBeSplit.to, Weight(1), true, recurrentPost))
	 		val updatedConnectionList = subnet.connections + (s.connectionToBeSplit.id -> subnet.connections(s.connectionToBeSplit.id).copy(enabled = false)) 
	 		val updatedSubnet = subnet.copy(id = s.subnetId, connections = updatedConnectionList + newPrior + newPost, neurons =  subnet.neurons + newNeuron  )
	 		val updatedSubnetList = subnetList + (updatedSubnet.id -> updatedSubnet) - {if(s.originalRequest.existingNetId != updatedSubnet.id) {s.originalRequest.existingNetId} else{ -1}}
	 		val updatedNeurons = neurons + (s.originalRequest.neuronId -> neurons(s.originalRequest.neuronId).copy(subnetId = Some(s.subnetId)))
	 		this.copy(neurons = updatedNeurons, subnets = Some(updatedSubnetList))

		}).getOrElse(this)
		
	}



}

