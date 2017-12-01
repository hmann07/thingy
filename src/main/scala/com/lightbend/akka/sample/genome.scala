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
case class ConnectionGenome(id: Int, from: Int, to: Int, weight: Option[Double])
case class NetworkGenome(id: Int, neurons: Seq[NeuronGenome], connections: Seq[ConnectionGenome], subnets: Option[Seq[NetworkGenome]]) {

	 def innovationHash: Set[Int] = {
	 	connections.map(_.id).toSet
	 }

	 def updateNetworkGenome(s: Innovation.InnovationConfirmation) = {
	 	val newConnection = ConnectionGenome(s.id, s.from, s.to, None)
	 	this.copy(connections = newConnection +: connections )

	 }


	/*
	 * Network genome has a specific method generate Actors so that subnetworks and networks can
	 * generate actor networks from the class based genome rather than directly from Json.
	 */

	 def generateActors(context: ActorContext) = {
	 	val neuronActors = neurons.foldLeft(NetworkNodeSchema()){ (schemaObj, current) =>
		 	current.subnetId match {

			 	case Some(subnet) =>
				 	// Here we should probably check that the filter actually returns something...
				 	val subnetStructure = subnets.get.filter( sn => {
					 sn.id == subnet
				 })(0)

				 val sn: ActorRef = context.actorOf(SubNetwork.props("subnet-" + current.name, subnetStructure), "subnet-" + current.name)
				 schemaObj.update(current, sn)
			 case None =>
				 val ar: ActorRef = context.actorOf(Props[Neuron], current.name)
				 schemaObj.update(current, ar)
		 	 }
	 	}

	 	val connectionConfigs = connections.foldLeft(Map[ActorRef, Neuron.ConnectionConfig]()) { (acc, current) =>
		 	val pre = Predecessor(neuronActors.allNodes(current.from))
		 	val suc = Successor(neuronActors.allNodes(current.to), Weight())
		 	val updateIncoming: Map[ActorRef, Neuron.ConnectionConfig] = acc get neuronActors.allNodes(current.to).actor match {

			 	case Some(a) => {
				 	val exisitingConfig = a
				 	acc + (neuronActors.allNodes(current.to).actor -> exisitingConfig.copy(inputs = pre :: exisitingConfig.inputs))
				 }
			 	case None => acc + (neuronActors.allNodes(current.to).actor -> Neuron.ConnectionConfig(inputs = List(pre)))
		 	}

		 updateIncoming get neuronActors.allNodes(current.from).actor match {
			 case Some(a) => {
				 val exisitingConfig = a

				 updateIncoming + (neuronActors.allNodes(current.from).actor ->  exisitingConfig.copy(outputs = suc :: exisitingConfig.outputs))
				 }
			 case None => updateIncoming + (neuronActors.allNodes(current.from).actor -> Neuron.ConnectionConfig(outputs = List(suc)))
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
 (JsPath  \ "weight").readNullable[Double]
) (ConnectionGenome.apply _)

implicit lazy val networkReads: Reads[NetworkGenome] = (
 (JsPath \ "id").read[Int] and
 (JsPath \ "neurons").read[Seq[NeuronGenome]] and
 (JsPath \ "connections").read[Seq[ConnectionGenome]] and
 (JsPath \ "subnets").lazyReadNullable[Seq[NetworkGenome]](Reads.seq(networkReads))
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
