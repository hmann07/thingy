package com.thingy.population

import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.genome.NetworkGenomeBuilder
import com.thingy.genome.NetworkGenome
import com.thingy.network.Network
import com.thingy.subnetwork.SubNetwork
import com.thingy.innovation._
import com.thingy.agent._
import com.thingy.species.Species
import com.thingy.species.SpeciesDirectory


sealed trait PopulationState
case object Initialising extends PopulationState
case object Active extends PopulationState


object Population {

	


	case class PopulationSettings(
			agentsCompleteCount: Int = 0, 
			agentsComplete: Map[ActorRef, NetworkGenome.NetworkGenome] = Map.empty,
			agentSumTotalFitness: Double = 0.0,
			currentSpecies: Int = 0,
			speciesDirectory: SpeciesDirectory = SpeciesDirectory())

	implicit val config = ConfigFactory.load()
}

class Population() extends FSM[PopulationState, Population.PopulationSettings] {
	import Population._

	val gNet = new NetworkGenomeBuilder()
 	val innovation = context.actorOf(Innovation.props(gNet), "innov8")
 	val p = config.getConfig("thingy").getInt("population-size")


 	for {
 		i <- 1 to p
 	}
 	yield {
 		context.actorOf(Agent.props(innovation, gNet), "agent" + i)
	}

	log.info("population created with {} children", context.children.size) 


	startWith(Active, PopulationSettings())

	when(Active) {
 		case Event(d: Network.Done, s: PopulationSettings) =>
 			
 			val completed = s.agentsCompleteCount + 1
				
			log.debug("population received Performance value of {} for genome: {}. received {} of {} ", d.performanceValue, d.genome.toJson, completed, p)

			// decide species.

			val newSpeciesDir = s.speciesDirectory.allocate(d.genome, d.performanceValue)
			log.debug("speciesDirectory is {}", newSpeciesDir)
 			


 			if(completed == p) {
 				// so....  we have got n genomes, each with a Performance value of some sort....
 				// time to select the best in line with their performance and send the genome back to agent whi should forward on to the network
 				
 				log.debug("population: All Agents Completed")

 				// time to select or allocate the best genomes for mating..

 				val survivors = s.speciesDirectory.selectGenerationSurvivors
 				log.debug("survivors. find a better name. : {}", survivors)

 			}
 			
 			stay using s.copy(agentsCompleteCount = completed, 	speciesDirectory = newSpeciesDir)
 	}




 	
}