package com.thingy.mutator

import com.thingy.genome.NetworkGenome
import com.thingy.innovation.Innovation
import scala.util.Random


class Mutator {

	/* 
	 * Types of Mutation
	 * Add Connection to Network
	 * Add Connection to SubNetwork
	 * Add Neuron to Network
	 * Add Neuron to SubNetwork
	 * Change weight
	 */


	def mutate(genome: NetworkGenome.NetworkGenome): Innovation.InnovationType = {


		val mutationFunctions = List(
				addNetworkConnection(_), 
				addSubNetConnection(_))

		mutationFunctions(Random.nextInt(mutationFunctions.length))(genome)
	}


	/* 
	 * Process:
	 * Find 2 Nodes that were previously unconnected. i.e. 
	 * pick two neurons. 
	 * make sure they are not inputs
	 * make sure they are not connected
	 */

	def addNetworkConnection(genome: NetworkGenome.NetworkGenome): Innovation.NetworkConnectionInnovation = {

		val neuronCount = genome.neurons.length
		val node1 = genome.neurons(Random.nextInt(neuronCount)).id
		val node2 = genome.neurons(Random.nextInt(neuronCount)).id

		if(genome.connections.exists(c => c.from == node1 && c.to == node2)) {
			addNetworkConnection(genome) //try again
		} else {
			Innovation.NetworkConnectionInnovation(node1,node2)
		}

	}


	/* 
	 * Process:
	 * randomly pick a subnet
	 * Find 2 Nodes in the subnetwork that were previously unconnected. i.e. 
	 * pick two neurons. 
	 * make sure they are not inputs
	 * make sure they are not connected
	 */
	def addSubNetConnection(genome: NetworkGenome.NetworkGenome): Innovation.SubNetConnectionInnovation = {

		val subnetCount = genome.subnets.get.length 
		val subnet = genome.subnets.get(Random.nextInt(subnetCount))
		val neuronCount = subnet.neurons.length
		val node1 = subnet.neurons(Random.nextInt(neuronCount)).id
		val node2 = subnet.neurons(Random.nextInt(neuronCount)).id

		if(subnet.connections.exists(c => c.from == node1 && c.to == node2)) {
			addSubNetConnection(genome) //try again
		} else {
			Innovation.SubNetConnectionInnovation(node1, node2, subnet.innovationHash, subnet.id, subnet.parentId.get)
		}
	}

	/*
	 * Add Neuron
	 * Pick a connection randomly
	 * Insert a Neuron 
	 * soft delete exisitng connection. Such that it may be enabled again at some point.
	 */


	 def addNetworkNode(genome: NetworkGenome.NetworkGenome) = {

	 	val connectionCount = genome.connections.length
	 	val connectionToSplit = genome.connections(Random.nextInt(connectionCount))

	 	// no point evolving disabled connections right?
	 	
	 	if(connectionToSplit.enabled) {
	 		Innovation.NetworkNeuronInnovation(connectionToSplit.id)
	 	} else {
			addNetworkNode(genome)	 		
	 	}

	 }




}