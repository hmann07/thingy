package com.thingy.genome

import akka.actor.{ ActorRef, FSM, Props, ActorContext }
import com.thingy.neuron.Neuron
import com.thingy.node._


case class NetworkNodeSchema(
		in: InputNodes = InputNodes(),// REMOVING SINCE NO LONGER REQUIRED.
		out: OutputNodes = OutputNodes(), // THIS could be hidden to
		hidden: HiddenNodes = HiddenNodes(),
		allNodes: Map[Int, Node] = Map.empty,
		inNodes: Map[Int, Map[ActorRef, Double]] = Map.empty
	){


		// takes the new actor created and updates the schema objec tthat acts as a directory of actors.
		def update(n: NeuronGenome, a: ActorRef): NetworkNodeSchema = {
			val newnode = Node(n.id, a)
			n.layer match {
			
			// DO INPUTS GO IN THIS SCHEMA?
			// INPUTS
			case 0 => copy(
				in = in.copy(nodes = newnode :: in.nodes),
				allNodes = allNodes + (n.id -> newnode))

			//OUTPUTS
			case 1 => copy(
							
							out = out.copy(nodes = newnode :: out.nodes ), 
							allNodes = allNodes + (n.id -> newnode))


			//ALL OTHER "HIDDEN"	
			case _ => copy(
					hidden = hidden.copy(nodes = newnode :: hidden.nodes ), 
					allNodes = allNodes + (n.id -> newnode)
				)
			}
		}
	}