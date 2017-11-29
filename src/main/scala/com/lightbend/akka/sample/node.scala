package com.thingy.node

import akka.actor.{ ActorRef}

case class Node (

	 id: Int,
	 actor: ActorRef
)

trait NodeList

case class InputNodes(nodes: List[Node] = List.empty) extends NodeList

case class HiddenNodes(nodes: List[Node] = List.empty) extends NodeList

case class OutputNodes(nodes: List[Node] = List.empty) extends NodeList
