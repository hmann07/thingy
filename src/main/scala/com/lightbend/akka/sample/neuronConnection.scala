package com.thingy.neuron

import akka.actor.{ ActorRef, FSM }
import com.thingy.weight.Weight
import com.thingy.node.Node

/**
  * AdjacentNeurons refer to all neurons in the network that are adjacent
  * by virtue of there being a directed edge connecting them to the
  * current Neuron of focus.
  *
  * The information here is used by the neuron to send or receive messages.
  * This is also the key location where weight information is stored. Weights will be applied to
  * the signal as it is sent. Not when it is received hence why Knowledge of Predecessors does
  * not include weight information.
  */

trait AdjacentNeuron

case class Predecessor(
	node: Node,
  recurrent: Boolean = false) extends AdjacentNeuron

case class Successor(
	node: Node,
	weight: Weight = Weight(),
  recurrent: Boolean) extends AdjacentNeuron
