package com.thingy.genome

import com.typesafe.config.ConfigFactory
import play.api.libs.json._
import java.io.FileInputStream
import play.api.libs.functional.syntax._
import akka.actor.{ ActorRef, FSM, Props, ActorContext }
import com.thingy.neuron.Neuron
import com.thingy.network._
import com.thingy.subnetwork._
import play.api.libs.json._
import com.thingy.neuron.{Successor, Predecessor}
import com.thingy.weight.Weight
import com.thingy.node._
import com.thingy.innovation.Innovation
import play.api.libs.json.Json.JsValueWrapper




object NetworkGenome {


case class NetworkNodeSchema(
	in: InputNodes = InputNodes(),
	out: OutputNodes = OutputNodes(),
	hidden: HiddenNodes = HiddenNodes(),
	allNodes: Map[Int, Node] = Map.empty){

	def update(n: NeuronGenome, a: ActorRef): NetworkNodeSchema = {
		val newnode = Node(n.id, a)
		n.layer match {
		case 0 => NetworkNodeSchema(
			in.copy(nodes = newnode :: in.nodes),
			out,
			hidden,
			allNodes + (n.id -> newnode))
		case 1 => NetworkNodeSchema(in, out.copy(nodes = newnode :: out.nodes ), hidden, allNodes + (n.id -> newnode))
		case _ => NetworkNodeSchema(in, out, hidden.copy(nodes = newnode :: hidden.nodes ), allNodes + (n.id -> newnode))
		}
	}
}

case class NeuronGenome(id: Int, name: String, layer: Int, activationFunction: Option[String], subnetId: Option[Int])
case class ConnectionGenome(id: Int, from: Int, to: Int, weight: Option[Double], enabled: Boolean = true)
case class NetworkGenome(id: Int, neurons: Map[Int, NeuronGenome], connections: Map[Int, ConnectionGenome], subnets: Option[Map[Int, NetworkGenome]], parentId: Option[Int]) {

	 def innovationHash: Set[Int] = {
	 	connections.values.map(_.id).toSet
	 }



	 def updateNetworkGenome(s: Innovation.InnovationConfirmation) = {
	 	val newConnection = (s.id -> ConnectionGenome(s.id, s.from, s.to, None))
	 	this.copy(connections =  connections + newConnection )

	 }

	 def updateNetworkGenome(s: Innovation.NetworkNeuronInnovationConfirmation) = {
	 	val neuronLayer = 1
	 	val newNeuron = (s.nodeid -> NeuronGenome(s.nodeid, "newNeuron" + s.nodeid, neuronLayer, Some("SIGMOID"), None))
	 	val newPrior = (s.priorconnectionId -> ConnectionGenome(s.priorconnectionId, s.connectionToBeSplit.from, s.nodeid, None))
	 	val newPost = (s.postconnectionId -> ConnectionGenome(s.postconnectionId, s.nodeid, s.connectionToBeSplit.to, None))
	 	val updatedConnectionList = connections.foldLeft(Map[Int, ConnectionGenome]()) { (conns, current) =>
	 		val currentObj = current._2
	 			conns + (current._1 -> {if (currentObj.id == s.connectionToBeSplit.id) {
	 				currentObj.copy(enabled = false)
	 			} else {currentObj}}) 
	 	}
	 	this.copy(connections = updatedConnectionList + newPrior + newPost, neurons =  neurons + newNeuron  )

	 }

	 def updateSubnet(s: Innovation.SubnetConnectionInnovationConfirmation) = {
	 	val updatedSubnets = subnets.get.foldLeft(Map[Int, NetworkGenome]()) { (subnets, current) =>
	 		val currentObj = current._2
	 		val updatedNet = if(currentObj.id == s.originalRequest.existingNetId) {
	 			val newConnection = (s.updatedConnectionTracker -> ConnectionGenome(s.updatedConnectionTracker, s.originalRequest.from, s.originalRequest.to, None))
	 			(currentObj.id ->  currentObj.copy(id = s.updatedNetTracker, connections = currentObj.connections + newConnection))
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


	/*
	 * Network genome has a specific method generate Actors so that subnetworks and networks can
	 * generate actor networks from the class based genome rather than directly from Json.
	 */

	 def generateActors(context: ActorContext): NetworkNodeSchema = {
	 	val neuronActors = neurons.foldLeft(NetworkNodeSchema()){ (schemaObj, current) =>
	 		val currentObj = current._2
		 	currentObj.subnetId match {

			 	case Some(subnet) =>
				 	// Here we should probably check that the filter actually returns something...
				 	val subnetStructure = subnets.get(subnet)

				 val sn: ActorRef = context.actorOf(SubNetwork.props("subnet-" + currentObj.name, subnetStructure), "subnet-" + currentObj.name)
				 schemaObj.update(currentObj, sn)
			 case None =>
				 val ar: ActorRef = context.actorOf(Props[Neuron], currentObj.name)
				 schemaObj.update(currentObj, ar)
		 	 }
	 	}

	 	val connectionConfigs = connections.foldLeft(Map[ActorRef, Neuron.ConnectionConfig]()) { (acc, current) =>
	 		val currentObj = current._2
		 	val pre = Predecessor(neuronActors.allNodes(currentObj.from))
		 	val suc = Successor(neuronActors.allNodes(currentObj.to), Weight())
		 	val updateIncoming: Map[ActorRef, Neuron.ConnectionConfig] = acc get neuronActors.allNodes(currentObj.to).actor match {

			 	case Some(a) => {
				 	val exisitingConfig = a
				 	acc + (neuronActors.allNodes(currentObj.to).actor -> exisitingConfig.copy(inputs = pre :: exisitingConfig.inputs))
				 }
			 	case None => acc + (neuronActors.allNodes(currentObj.to).actor -> Neuron.ConnectionConfig(inputs = List(pre)))
		 	}

		 updateIncoming get neuronActors.allNodes(currentObj.from).actor match {
			 case Some(a) => {
				 val exisitingConfig = a

				 updateIncoming + (neuronActors.allNodes(currentObj.from).actor ->  exisitingConfig.copy(outputs = suc :: exisitingConfig.outputs))
				 }
			 case None => updateIncoming + (neuronActors.allNodes(currentObj.from).actor -> Neuron.ConnectionConfig(outputs = List(suc)))
			 }
	 	}

	 	// Maps of actor and their connection config has been created.
	 	// It's now possible to send the configs to the actors.

	 	connectionConfigs.foreach(c => {
			c._1 ! c._2
	 	})
	neuronActors
   	}
}

class MapIntReads[T]()(implicit reads: Reads[T]) extends Reads[Map[Int, T]] {
  def reads(jv: JsValue): JsResult[Map[Int, T]] =
    JsSuccess(jv.as[Map[String, T]].map{case (k, v) =>
      k.toString.toInt -> v .asInstanceOf[T]
    })
}

class MapIntWrites[T]()(implicit writes: Writes[T])  extends Writes[Map[Int, T]] {
  def writes(map: Map[Int, T]): JsValue =
    Json.obj(map.map{case (s, o) =>
      val ret: (String, JsValueWrapper) = s.toString -> Json.toJson(o)
      ret
    }.toSeq:_*)
}

implicit val mapIntNeuron: Reads[Map[Int, NeuronGenome]] = new MapIntReads[NeuronGenome]
implicit val mapIntConnection: Reads[Map[Int, ConnectionGenome]] = new MapIntReads[ConnectionGenome]
implicit val mapIntSubnet: Reads[Map[Int, NetworkGenome]] = new MapIntReads[NetworkGenome]

implicit val neruonReads: Reads[NeuronGenome] = (
 (JsPath \ "id").read[Int] and
 (JsPath  \ "name").read[String] and
 (JsPath  \ "layer").read[Int] and
 (JsPath  \ "activationFunction").readNullable[String] and
 (JsPath  \ "subnetid").readNullable[Int]

) (NeuronGenome.apply _)

implicit val connectionReads: Reads[ConnectionGenome] = (
 (JsPath \ "id").read[Int] and
 (JsPath  \ "from").read[Int] and
 (JsPath  \ "to").read[Int] and
 (JsPath  \ "weight").readNullable[Double] and
 (JsPath  \ "enabled").read[Boolean]
) (ConnectionGenome.apply _)

implicit lazy val networkReads: Reads[NetworkGenome] = (
 (JsPath \ "id").read[Int] and
 (JsPath \ "neurons").read[Map[Int, NeuronGenome]] and
 (JsPath \ "connections").read[Map[Int, ConnectionGenome]] and
 (JsPath \ "subnets").lazyReadNullable[Map[Int, NetworkGenome]] and
 (JsPath  \ "parentId").readNullable[Int]
) (NetworkGenome.apply _)


}

class NetworkGenomeBuilder {

	import NetworkGenome._
	implicit val config = ConfigFactory.load()
	val file = config.getConfig("thingy").getString("seed-network")
	val stream = new FileInputStream(file)
	val json = try {  Json.parse(stream) } finally { stream.close() }

	//log.debug("loaded json file {}", json)

	def generateFromSeed: NetworkGenome = {

		json.validate[NetworkGenome] match {

		  case g: JsSuccess[NetworkGenome] => {
				val genome = g.get
				genome
			}
		}
	}
}
