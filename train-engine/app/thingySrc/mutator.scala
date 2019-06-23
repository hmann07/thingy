package com.thingy.mutator

import com.thingy.config.ConfigDataClass.ConfigData
import com.thingy.genome.NetworkGenome
import com.thingy.genome.ConnectionGenome
import com.thingy.genome.NeuronGenome
import com.thingy.innovation.Innovation
import scala.util.Random
import com.typesafe.config.ConfigFactory

class Mutator(configData: ConfigData) {

	/* 
	 * Types of Mutation
	 * Add Connection to Network
	 * Add Connection to SubNetwork
	 * Add Neuron to Network
	 * Add Neuron to SubNetwork
	 * Change weight
	 */
	
	// perhaps necessary to check the sum = 1.
	val mutationFunctions = List(
				(addNetworkNode(_), configData.addNetworkNode), 
				//addSubNetConnection(_), configData.addSubNetConnection,
				(addNetworkConnection(_),configData.addNetworkConnection),
				//addSubNetworkNode(_),
				(mutateWeights(_), configData.mutateWeights)
			)

		

	def mutate(genome: NetworkGenome): Innovation.InnovationType = {

		def getfn(fnList: List[(NetworkGenome => Innovation.InnovationType, Double)], cumulativeSum: Double = 0.0): NetworkGenome => Innovation.InnovationType = {
			val mThrow = Random.nextDouble()
			fnList.headOption.map(fnItem => if(mThrow < cumulativeSum + fnItem._2) fnItem._1 else getfn(fnList.tail, cumulativeSum + fnItem._2)).getOrElse(mutateWeights(_))
		}

		getfn(mutationFunctions)(genome)
	}


	/* 
	 * Process:
	 * Find 2 Nodes that were previously unconnected. i.e. 
	 * pick two neurons. 
	 * make sure they are not inputs
	 * make sure they are not connected
	 */

	def addNetworkConnection(genome: NetworkGenome): Innovation.InnovationType = {

		

		def addNetworkConnectionInt(genome: NetworkGenome , tries: Int): Innovation.InnovationType = {

			if(tries == 0) {
				mutateWeights(genome)
			} else {
				
				val neuronCount = genome.neurons.size
				val node1 = genome.neurons.values.toList(Random.nextInt(neuronCount))
				val node2 = genome.neurons.values.toList(Random.nextInt(neuronCount))

				if(genome.connections.exists(c => c._2.from == node1.id && c._2.to == node2.id) || node2.layer == 0.00000 || node2.layer <= node1.layer ) {
					addNetworkConnectionInt(genome, tries -1) //try again
				} else {
					Innovation.NetworkConnectionInnovation(node1.id,node2.id)
				}
			}
		}

		addNetworkConnectionInt(genome, 100)
	}


	/* 
	 * Process:
	 * randomly pick a subnet
	 * Find 2 Nodes in the subnetwork that were previously unconnected. i.e. 
	 * pick two neurons. 
	 * make sure they are not inputs
	 * make sure they are not connected
	 */
	def addSubNetConnection(genome: NetworkGenome): Innovation.InnovationType = {

		

		def addSubNetConnectionInt(genome: NetworkGenome , tries: Int): Innovation.InnovationType = {

			if(tries == 0) {
				mutateWeights(genome)
			} else {

				val subnetCount = genome.subnets.get.size 
				val subnet = genome.subnets.get.values.toList(Random.nextInt(subnetCount))
				val neuronCount = subnet.neurons.size
				val node1 = subnet.neurons.values.toList(Random.nextInt(neuronCount))
				val node2 = subnet.neurons.values.toList(Random.nextInt(neuronCount))

				if(subnet.connections.exists(c => c._2.from == node1.id && c._2.to == node2.id) || node2.layer == 0) {
					addSubNetConnectionInt(genome, tries -1) //try again
				} else {
					Innovation.SubNetConnectionInnovation(node1.id, node2.id, subnet.innovationHash, subnet.id, subnet.parentId.get)
				}
			}
		}

		addSubNetConnectionInt(genome, 10)
	}

	/*
	 * Add Neuron
	 * Pick a connection randomly
	 * Insert a Neuron 
	 * soft delete exisitng connection. Such that it may be enabled again at some point.
	 */


	 def addNetworkNode(genome: NetworkGenome): Innovation.InnovationType = {

	 	def addNetworkNodeInt(genome: NetworkGenome , tries: Int): Innovation.InnovationType = {

			if(tries == 0) {
				mutateWeights(genome)
			} else {

			 	val connectionCount = genome.connections.size
			 	val connectionToSplit = genome.connections.values.toList(Random.nextInt(connectionCount))

			 	// no point evolving disabled connections right?
			 	// temporarily... let's not add neurons on recursive connections.. it's a headache... 
			 	if(connectionToSplit.enabled && !connectionToSplit.recurrent && (connectionToSplit.from != connectionToSplit.to) ) {
			 		Innovation.NetworkNeuronInnovation(connectionToSplit)
			 	} else {
					addNetworkNodeInt(genome, tries)	 		
			 	}
		 	}

	 	}

	 	addNetworkNodeInt(genome, 100)

	}

	/*
	 * Pick A connection.
	 * inspect both src and dest node. 
	 * If src has > 1 enabled ouput AND dest has > 1 enabled input.
	 * delete the connection (or just disable it)
	 */


	def removeNetworkConnection(genome: NetworkGenome) {

	}

	/*
	 * Pick A Neuron.
	 * inspect both src and dest nodes.
	 * create connections (fully connected) between src and destinnation (checking to ensure there's not one already.) 
	 * SharpNeat takes the approach of identifing only neurons with one connection on one side... i.e
	 * - Find a neuron that satisfies: One inputs connection OR One Output connection. 
	 * - Replace that neuron with connections that = the count of connections on the other side. 
	 * Innovation numbers will need to be sought for the new connections.
	 * delete the node
	 */


	def removeNetworkNode(genome: NetworkGenome) {
		
		val connectionCount = genome.neurons.size
		val nodeToRemove = genome.neurons.values.toList(Random.nextInt(connectionCount))
	}

	 

	 /*
	 * Add Neuron
	 * Pick a subnetwork. 
	 * Pick a connection randomly.
	 * Insert a Neuron 
	 * soft delete exisitng connection. Such that it may be enabled again at some point.
	 */

	 def addSubNetworkNode(genome: NetworkGenome): Innovation.SubNetNeuronInnovation = {

	 	val subnetCount = genome.subnets.get.size 
		val subnet = genome.subnets.get.values.toList(Random.nextInt(subnetCount))

	 	val connectionCount = subnet.connections.size
	 	val connectionToSplit = subnet.connections.values.toList(Random.nextInt(connectionCount))

	 	// no point evolving disabled connections right?
	 	
	 	if(connectionToSplit.enabled) {
	 		Innovation.SubNetNeuronInnovation(connectionToSplit, subnet.innovationHash, subnet.id, subnet.parentId.get )
	 	} else {
	 		// yeh this is dogy and may cause overflows....
			addSubNetworkNode(genome)	 		
	 	}

	 }

	 def mutateWeights(genome: NetworkGenome): Innovation.WeightChangeInnovation = {
	 	val newG = genome.connections.foldLeft(Map[Int, ConnectionGenome]()){
	 		(acc, current) => {
	 			if(Random.nextDouble < configData.weightMutationRate) {
	 			val (key, genome) = current
	 			acc + (key -> genome.copy(weight = genome.weight.mutate))
	 			} else {
	 			acc + current
	 			}

	 		}
	 	}
	 	val newN = genome.neurons.foldLeft(Map[Int, NeuronGenome]()){
	 		(acc, current) => {
	 			if(Random.nextDouble < configData.weightMutationRate) {
	 			val (key, genome) = current
	 			acc + (key -> genome.copy(biasWeight = genome.biasWeight.mutate))
	 			} else {
	 			acc + current
	 			}

	 		}
	 	}
	 	Innovation.WeightChangeInnovation(genome.copy(connections= newG, neurons = newN))
	 }




}