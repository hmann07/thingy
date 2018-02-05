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
				addSubNetConnection(_),
				addNetworkNode(_),
				addSubNetworkNode(_)
			)
		
		mutationFunctions(Random.nextInt(mutationFunctions.length))(genome)
	}


	/* 
	 * Process:
	 * Find 2 Nodes that were previously unconnected. i.e. 
	 * pick two neurons. 
	 * make sure they are not inputs
	 * make sure they are not connected
	 */

	def addNetworkConnection(genome: NetworkGenome.NetworkGenome): Innovation.InnovationType = {

		

		def addNetworkConnectionInt(genome: NetworkGenome.NetworkGenome , tries: Int): Innovation.InnovationType = {

			if(tries == 0) {
				Innovation.WeightChangeInnovation()
			} else {
				
				val neuronCount = genome.neurons.size
				val node1 = genome.neurons.values.toList(Random.nextInt(neuronCount))
				val node2 = genome.neurons.values.toList(Random.nextInt(neuronCount))

				if(genome.connections.exists(c => c._2.from == node1.id && c._2.to == node2.id) || node2.layer == 0) {
					addNetworkConnectionInt(genome, tries -1) //try again
				} else {
					Innovation.NetworkConnectionInnovation(node1.id,node2.id)
				}
			}
		}

		addNetworkConnectionInt(genome, 10)
	}


	/* 
	 * Process:
	 * randomly pick a subnet
	 * Find 2 Nodes in the subnetwork that were previously unconnected. i.e. 
	 * pick two neurons. 
	 * make sure they are not inputs
	 * make sure they are not connected
	 */
	def addSubNetConnection(genome: NetworkGenome.NetworkGenome): Innovation.InnovationType = {

		

		def addSubNetConnectionInt(genome: NetworkGenome.NetworkGenome , tries: Int): Innovation.InnovationType = {

			if(tries == 0) {
				Innovation.WeightChangeInnovation()
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


	 def addNetworkNode(genome: NetworkGenome.NetworkGenome): Innovation.NetworkNeuronInnovation = {

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

	 def addSubNetworkNode(genome: NetworkGenome.NetworkGenome): Innovation.SubNetNeuronInnovation = {

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




}