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

	 def toJson = {
	 	Json.toJson(this)
	 }

	 /*
	  * This function will update the genome by adding a new connection based on
	  * the innovation confirmation which will include the correct id
	  * and the relevant from and to nodes.
	  */
	 def updateNetworkGenome(s: Innovation.InnovationConfirmation): NetworkGenome  = {
	 	val newConnection = (s.id -> ConnectionGenome(s.id, s.from, s.to, None))
	 	this.copy(connections =  connections + newConnection )
	 }


     /*
	  * This updateNetworkGenome function will update the genome by adding a new new node based on
	  * the innovation confirmation. The layer id will need to be computed based on the incoming and outoging nodes
	  * Two new connections will be created and the original connection disabled.
	  */
	 def updateNetworkGenome(s: Innovation.NetworkNeuronInnovationConfirmation): NetworkGenome = {
	 	val neuronLayer = (neurons(s.connectionToBeSplit.from).layer + neurons(s.connectionToBeSplit.to).layer) / 2
	 	val newNeuron = (s.nodeid -> NeuronGenome(s.nodeid, "newNeuron" + s.nodeid, neuronLayer, Some("SIGMOID"), None))
	 	val newPrior = (s.priorconnectionId -> ConnectionGenome(s.priorconnectionId, s.connectionToBeSplit.from, s.nodeid, None))
	 	val newPost = (s.postconnectionId -> ConnectionGenome(s.postconnectionId, s.nodeid, s.connectionToBeSplit.to, None))
	 	val updatedConnectionList = connections + (s.connectionToBeSplit.id -> connections(s.connectionToBeSplit.id).copy(enabled = false))
	 	
	 	/*
	 	val updatedConnectionList = connections.foldLeft(Map[Int, ConnectionGenome]()) { (conns, current) =>
	 		val currentObj = current._2
	 			conns + (current._1 -> {if (currentObj.id == s.connectionToBeSplit.id) {
	 				currentObj.copy(enabled = false)
	 			} else {currentObj}}) 
	 	}
	 	*/
	 	
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

	def updateSubnet(s: Innovation.SubNetNeuronInnovationConfirmation): NetworkGenome = {
		subnets.map(subnetList => {
		
			val subnet = subnetList(s.originalRequest.existingNetId)
			val neuronLayer = (subnet.neurons(s.connectionToBeSplit.from).layer + subnet.neurons(s.connectionToBeSplit.to).layer) / 2
	 		val newNeuron = (s.nodeid -> NeuronGenome(s.nodeid, "newNeuron" + s.nodeid, neuronLayer, Some("SIGMOID"), None))
	 		val newPrior = (s.priorconnectionId -> ConnectionGenome(s.priorconnectionId, s.connectionToBeSplit.from, s.nodeid, None))
	 		val newPost = (s.postconnectionId -> ConnectionGenome(s.postconnectionId, s.nodeid, s.connectionToBeSplit.to, None))
	 		val updatedConnectionList = subnet.connections + (s.connectionToBeSplit.id -> subnet.connections(s.connectionToBeSplit.id).copy(enabled = false)) 

	 		val updatedSubnet = subnet.copy(connections = updatedConnectionList + newPrior + newPost, neurons =  neurons + newNeuron  )
	 		val updatedSubnetList = subnetList + (updatedSubnet.id -> updatedSubnet)

	 		this.copy(subnets = Some(updatedSubnetList))

		}).get
		
	}


	/*
	 * Network genome has a specific method generate Actors so that subnetworks and networks can
	 * generate actor networks from the class based genome rather than directly from Json.
	 * Logic is added to check the schema to see if the node already exists or not.
	 */

	 def generateActors(context: ActorContext, networkSchema: NetworkNodeSchema): NetworkNodeSchema = {
	 	val neuronActors: NetworkNodeSchema = neurons.foldLeft(networkSchema){ (schemaObj, current) =>
	 		val currentObj = current._2
		 	currentObj.subnetId match {

			 	case Some(subnet) =>
				 	
				 	// already an actor for this genome?
				 	{if(!schemaObj.allNodes.contains(currentObj.id)){
				 		val subnetStructure = subnets.get(subnet)
					    val sn: ActorRef = context.actorOf(SubNetwork.props("subnet-" + currentObj.name, subnetStructure), "subnet-" + currentObj.name)
					    schemaObj.update(currentObj, sn)
				 	} else {
				 		schemaObj
				 	}}
				 	
			 case None =>
			 		// already an actor for this genome?
				 	{if(!schemaObj.allNodes.contains(currentObj.id)){
				 		val ar: ActorRef = context.actorOf(Props[Neuron], currentObj.name)
				 		schemaObj.update(currentObj, ar)
		 	 		} else {
				 		schemaObj
				 	}}
	 		}
	 	}

	 	val connectionConfigs = connections.foldLeft(Map[ActorRef, Neuron.ConnectionConfig]()) { (acc, current) =>
	 		val currentObj = current._2
	 		if (currentObj.enabled) {
	 			// If the connection is enabled update the config obj. 
		 		val pre = Predecessor(neuronActors.allNodes(currentObj.from))
		 		val suc = Successor(neuronActors.allNodes(currentObj.to), Weight())

		 		// Create configs for all inputs
		 		val updateIncoming: Map[ActorRef, Neuron.ConnectionConfig] = acc get neuronActors.allNodes(currentObj.to).actor match {
			 		case Some(existingConfig) => {
				 		acc + (neuronActors.allNodes(currentObj.to).actor -> existingConfig.copy(inputs = pre :: existingConfig.inputs))
				 	}
			 		case None => acc + (neuronActors.allNodes(currentObj.to).actor -> Neuron.ConnectionConfig(inputs = List(pre)))
		 		}

		 		// create config for all outputs
		 		updateIncoming get neuronActors.allNodes(currentObj.from).actor match {
			 		case Some(existingConfig) => {
				 		updateIncoming + (neuronActors.allNodes(currentObj.from).actor ->  existingConfig.copy(outputs = suc :: existingConfig.outputs))
				 	}
			 		case None => updateIncoming + (neuronActors.allNodes(currentObj.from).actor -> Neuron.ConnectionConfig(outputs = List(suc)))
			 	}
	 	} else {
			acc
		}}

		// Maps of actor and their connection config has been created.
		// It's now possible to send the configs to the actors.

		connectionConfigs.foreach(c => {
			c._1 ! c._2
	 	})
		neuronActors
   	}
}


implicit val neuronReads: Reads[NeuronGenome] = (
 (JsPath \ "id").read[Int] and
 (JsPath  \ "name").read[String] and
 (JsPath  \ "layer").read[Int] and
 (JsPath  \ "activationFunction").readNullable[String] and
 (JsPath  \ "subnetid").readNullable[Int]

) (NeuronGenome.apply _)


implicit val neuronWrites: Writes[NeuronGenome] = (
 (JsPath \ "id").write[Int] and
 (JsPath  \ "name").write[String] and
 (JsPath  \ "layer").write[Int] and
 (JsPath  \ "activationFunction").writeNullable[String] and
 (JsPath  \ "subnetid").writeNullable[Int]

) (unlift(NeuronGenome.unapply))




implicit val connectionReads: Reads[ConnectionGenome] = (
 (JsPath \ "id").read[Int] and
 (JsPath  \ "from").read[Int] and
 (JsPath  \ "to").read[Int] and
 (JsPath  \ "weight").readNullable[Double] and
 (JsPath  \ "enabled").read[Boolean]
) (ConnectionGenome.apply _)


implicit val connectionWrites: Writes[ConnectionGenome] = (
 (JsPath \ "id").write[Int] and
 (JsPath  \ "from").write[Int] and
 (JsPath  \ "to").write[Int] and
 (JsPath  \ "weight").writeNullable[Double] and
 (JsPath  \ "enabled").write[Boolean]
) (unlift(ConnectionGenome.unapply))


implicit lazy val networkReads: Reads[NetworkGenome] = (
 (JsPath \ "id").read[Int] and
 (JsPath \ "neurons").read[Seq[NeuronGenome]].map{_.foldLeft(Map[Int, NeuronGenome]()){(acc, current) => acc + (current.id -> current)}} and
 (JsPath \ "connections").read[Seq[ConnectionGenome]].map{_.foldLeft(Map[Int, ConnectionGenome]()){(acc, current) => acc + (current.id -> current)}} and
 (JsPath \ "subnets").lazyReadNullable[Seq[NetworkGenome]](Reads.seq(networkReads)).map{_.map{_.foldLeft(Map[Int, NetworkGenome]()){(acc, current) => acc + (current.id -> current)}}} and
 (JsPath  \ "parentId").readNullable[Int]
) (NetworkGenome.apply _)



/*
implicit lazy val networkWrites: Writes[NetworkGenome] = (
 (JsPath \ "id").write[Int] and
 (JsPath \ "neurons").write[Seq[NeuronGenome]] and
 (JsPath \ "connections").write[Seq[ConnectionGenome]] and
 (JsPath \ "subnets").lazyWriteNullable[Seq[NetworkGenome]](Writes.seq(networkWrites)) and
 (JsPath  \ "parentId").writeNullable[Int]
) (unlift(NetworkGenome.unapply))
*/

implicit val netWrites: Writes[NetworkGenome] = new Writes[NetworkGenome] {
    def writes(net: NetworkGenome): JsValue = Json.obj(
    	"id" -> net.id,
    	"neurons" -> net.neurons.values.map(n => Json.toJson(n)), 
    	"connections" -> net.connections.values.map(c => Json.toJson(c)), 
    	"subnets" -> net.subnets.map(os=> os.values.map(s => Json.toJson(s))),
    	"parent" -> net.parentId
        //bar.key -> Json.obj("value" -> bar.value)
    )
}


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
