package com.thingy.population

import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.genome.NetworkGenomeBuilder
import com.thingy.genome.NetworkGenome.NetworkGenome
import com.thingy.network.Network
import com.thingy.subnetwork.SubNetwork
import com.thingy.innovation._
import com.thingy.agent._
import com.thingy.species.Species
import com.thingy.species.SpeciesDirectory
import play.api.libs.json._


sealed trait PopulationState
case object Initialising extends PopulationState
case object Active extends PopulationState


object Population {

	


	case class PopulationSettings(
			currentGeneration: Int = 1,
			agentsCompleteCount: Int = 0, 
			agentsComplete: Map[ActorRef, NetworkGenome] = Map.empty,
			agentSumTotalFitness: Double = 0.0,
			currentSpecies: Int = 0,
			speciesDirectory: SpeciesDirectory = SpeciesDirectory())

	implicit val config = ConfigFactory.load()
}

class Population() extends FSM[PopulationState, Population.PopulationSettings] {
	import Population._

	val gNet = () => {new NetworkGenomeBuilder()}.generateFromSeed
 	val innovation = context.actorOf(Innovation.props(gNet()), "innov8")
 	val p = config.getConfig("thingy").getInt("population-size")
 	val generations = config.getConfig("thingy").getInt("generations")
 	def repurposeAgents(gestatable: List[()=>NetworkGenome]) = {
 		rep(gestatable, context.children.toList)
 	}

 	private def rep(g:List[()=>NetworkGenome], c: List[ActorRef]):Unit ={
		g.headOption.map(gnew=>{
			val evalG = gnew
			c.headOption.map(cnew=> {
				cnew ! Network.NetworkUpdate(evalG)
				rep(g.tail, c.tail)
			}).getOrElse(context.actorOf(Agent.props(innovation, evalG), "agent" + "weneedtospecanid"))
		}).getOrElse({
			c.foreach(newc => context.stop(newc))
		})	
	}

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
			
			val logParams = Array(s.currentGeneration, d.performanceValue, d.genome.toJson, completed, p)	
			log.debug("generation {} population received Performance value of {} for genome: {}. received {} of {} ", logParams)

			// decide species.

			val newSpeciesDir = s.speciesDirectory.allocate(d.genome, d.performanceValue)
			log.debug("speciesDirectory id number is {}", s.speciesDirectory.currentSpeciesId)
 			


 			if(completed == p) {
 				// so....  we have got n genomes, each with a Performance value of some sort....
 				// time to select the best in line with their performance and send the genome back to agent whi should forward on to the network
 					
 				//log.debug("population: All Agents Completed")
 				log.debug("generation {} completed", s.currentGeneration)
 				// time to select or allocate the best genomes for mating..
 				val resetSpeciesDir = s.speciesDirectory.reset
 				// check to see if we have done all generations

 				if (s.currentGeneration == generations) {
 					log.debug("All generations finished")
 					stay
 				} else {

 				val gestatable = s.speciesDirectory.selectGenerationSurvivors
 				log.debug("gestatable. find a better name. : {}", gestatable)

 				// Now we have a load of functions to run we need to send them to available agents. 
 				//creating new ones if required and shutting down old ones..

 				TODO: Handle populations that are diverging pop parameter

 				repurposeAgents(gestatable.flatten.toList)

 				stay using s.copy(
 								  currentGeneration = s.currentGeneration + 1,
 								  agentsCompleteCount = 0, 	
 								  agentSumTotalFitness = 0,
 								  speciesDirectory = resetSpeciesDir)
 				}
 			} else {	 
 			
 				stay using s.copy(agentsCompleteCount = completed, 	speciesDirectory = newSpeciesDir)
 			}
 	}




 	
}