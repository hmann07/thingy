package com.thingy.mutator

import com.thingy.genome.NetworkGenome
import com.thingy.genome.ConnectionGenome
import com.thingy.innovation.Innovation
import scala.util.Random
import com.typesafe.config.ConfigFactory

class Mutator {

	/* 
	 * Types of Mutation
	 * Add Connection to Network
	 * Add Connection to SubNetwork
	 * Add Neuron to Network
	 * Add Neuron to SubNetwork
	 * Change weight
	 */
	val config = ConfigFactory.load()

	def mutate(genome: NetworkGenome): Innovation.InnovationType = {


		val mutationFunctions = List(
				(addNetworkConnection(_),0.03), 
				//addSubNetConnection(_),
				(addNetworkNode(_),0.05),
				//addSubNetworkNode(_),
				(mutateWeights(_), 1.0)
			)
		
		val mThrow = Random.nextDouble()

		def getfn(fnList: List[(NetworkGenome => Innovation.InnovationType, Double)]): NetworkGenome => Innovation.InnovationType = {
			fnList.headOption.map(fnItem => if(mThrow < fnItem._2) fnItem._1 else getfn(fnList.tail)).getOrElse(mutateWeights(_))
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

				if(genome.connections.exists(c => c._2.from == node1.id && c._2.to == node2.id) || node2.layer == 0.00000) {
					addNetworkConnectionInt(genome, tries -1) //try again
				} else {
					Innovation.NetworkConnectionInnovation(node1.id,node2.id)
				}
			}
		}

		addNetworkConnectionInt(genome, 30)
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


	 def addNetworkNode(genome: NetworkGenome): Innovation.NetworkNeuronInnovation = {

	 	val connectionCount = genome.connections.size
	 	val connectionToSplit = genome.connections.values.toList(Random.nextInt(connectionCount))

	 	// no point evolving disabled connections right?
	 	
	 	if(connectionToSplit.enabled) {
	 		Innovation.NetworkNeuronInnovation(connectionToSplit)
	 	} else {
			addNetworkNode(genome)	 		
	 	}

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
	 			if(Random.nextDouble < config.getConfig("thingy").getDouble("weight-mutation-likelihood")) {
	 			val (key, genome) = current
	 			acc + (key -> genome.copy(weight = genome.weight.mutate))
	 			} else {
	 			acc + current
	 			}

	 		}
	 	}
	 	Innovation.WeightChangeInnovation(genome.copy(connections= newG))
	 }




}