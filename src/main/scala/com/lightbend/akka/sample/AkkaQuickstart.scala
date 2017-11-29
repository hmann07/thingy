package com.thingy.main

import akka.actor._
import kamon.Kamon
import com.thingy.neuron.Neuron
import com.thingy.neuron.Successor
import com.thingy.weight.Weight
import com.thingy.genome.{NetworkGenomeBuilder}
import com.thingy.network.Network
import com.thingy.subnetwork.SubNetwork
import com.thingy.innovation._

object Acne extends App {
  //Kamon.start()

// These two genomes are the seed genomes. In line with the NEAT plan these are the most simplistic
// The first network will consist of the most simple sub networks input -> output.
// These subnetworks wil complexify over time.

 val gNet = new NetworkGenomeBuilder()



 val system = ActorSystem()


val snInnovation = system.actorOf(Innovation.props(gNet), "innov8")

 val net = system.actorOf(Network.props("Init network", gNet), "network")

 net ! Neuron.Signal(10)
 snInnovation ! Innovation.NetworkConnectionInnovation(1,3)

 //val subnet = system.actorOf(SubNetwork.props("subnetwork"), "subnetwork")
 //val subnet2 = system.actorOf(SubNetwork.props("subnetwork2"), "subnetwork2")

//val snc = SubNetwork.ConnectionConfig(outputs = List(Successor(subnet2, Weight())))
//val snc2 = SubNetwork.ConnectionConfig()
//subnet ! snc
//subnet2 ! snc2

//subnet ! SubNetwork.Signal(10)

 //val n = system.actorOf(Props[Neuron], "test")
 //val n2 = system.actorOf(Props[Neuron], "test2")

 //val ns = Neuron.ConnectionConfig(outputs = List(Successor(n2, Weight())))
 //val ns2 = Neuron.ConnectionConfig()

 //n ! ns
 //n2 ! ns2
 //n ! Neuron.Signal(10)
 //n ! Neuron.Signal(10)
 //n ! Neuron.Signal(10)




  }
