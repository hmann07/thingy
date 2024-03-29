package com.thingy.population

import com.thingy.config.ConfigDataClass.{ConfigData, RuntimeConfig}
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, FSM, Props }
import akka.pattern.pipe
import com.thingy.genome.NetworkGenomeBuilder
import com.thingy.genome.NetworkGenome
import com.thingy.genome.NeuronGenome
import com.thingy.genome.ConnectionGenome
import com.thingy.weight.Weight
import com.thingy.genome.GenomeIO
import com.thingy.network.Network
import com.thingy.subnetwork.SubNetwork
import com.thingy.innovation._
import com.thingy.agent._
import com.thingy.species.SpeciesMember
import com.thingy.species.Species
import com.thingy.species.SpeciesDirectory
import com.thingy.environment.EnvironmentIOSpec
import com.thingy.environment.EnvironmentType
import com.thingy.environment.FileEnvironmentType
import play.api.libs.json._
import reactivemongo.play.json._
import play.api.libs.iteratee._
import java.util.Calendar

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.{ DefaultDB, MongoConnection, MongoDriver }
import reactivemongo.bson.{
  BSONWriter, BSONDocument, BSONDouble, BSONDocumentWriter, BSONDocumentReader, Macros, document
}

sealed trait PopulationState
case object Initialising extends PopulationState
case object Active extends PopulationState


object Population {

	def props(out: ActorRef, configData: ConfigData) = Props(new Population(out, configData)) 

	implicit val config = ConfigFactory.load()
	val p = config.getConfig("thingy").getInt("population-size")
	
	val mongoUri = "mongodb://localhost:27017/genomeData?authMode=scram-sha1"

 	 // Connect to the database: Must be done only once per application
  	val driver = MongoDriver()
  	val parsedUri = MongoConnection.parseURI(mongoUri)
  	val connection = parsedUri.map(driver.connection(_))
  	
  	// Database and collections: Get references
  	val futureConnection = Future.fromTry(connection)
  	def db1: Future[DefaultDB] = futureConnection.flatMap(_.database("genomeData"))
  	def genomeCollection = db1.map(_.collection("genomes"))
  	def generationCollection = db1.map(_.collection("generations"))
  	def environmentCollection = db1.map(_.collection("environments"))
  	def speciesCollection = db1.map(_.collection("species"))
  	def runStatsCollection = db1.map(_.collection("runs"))

	case class FrontendMessage(msg: String)
  	
  	case class EnvData(data: BSONDocument)

	case class PopulationSettings(
			currentGeneration: Int = 1,
			currentFamily: Int = 1,
			currentPopulation: List[ActorRef] = List.empty,
			currentPopulationSize: Int = p,
			agentsCompleteCount: Int = 0, 
			agentsComplete: Map[ActorRef, NetworkGenome] = Map.empty,
			agentSumTotalFitness: Double = 0.0,
			currentSpecies: Int = 0,
			speciesDirectory: SpeciesDirectory,
			innovation: ActorRef = null,
			envType: EnvironmentType = null)

	
}

class Population(out: ActorRef, configData: ConfigData, runtimeConfig: RuntimeConfig = RuntimeConfig()) extends FSM[PopulationState, Population.PopulationSettings] {
	import Population._

	//val gNet = () => {new NetworkGenomeBuilder(None)}.generate
	val startTime = Calendar.getInstance().getTimeInMillis()
	val runId = "evolutionrun¬"  + startTime


	out ! (runId)

	if(runtimeConfig.mongoStorage) {
		val query = BSONDocument("_id" -> Json.obj("$oid" -> configData.environmentId))
		val envData = environmentCollection.flatMap(_.find(query).one[BSONDocument])
		def extractEnvData(implicit ec: ExecutionContext) = { 

			envData.map {d=>
				EnvData(d.get)
			}
		}


		extractEnvData.pipeTo(self)
	}
	// else we assume that somehow this population will be sent som environment settings
	
 	val generations = configData.maxGenerations
 	
 	def repurposeAgents(gestatable: List[PopulationCandidate], population: List[ActorRef], innovation: ActorRef, envType:EnvironmentType) = {
 		rep(gestatable, population, List.empty, innovation, envType)
 	}



 	private def rep(g:List[PopulationCandidate], c: List[ActorRef], cummulate: List[ActorRef], innovation: ActorRef, envType: EnvironmentType):List[ActorRef] ={
		g.headOption.map(gnew => {

			val e = gnew match {
				case x: ElitePopulationCandidate => true
				case _ => false
			}

			val evalG = new GenomeIO(None, Some(gnew.genomeFunction))
			c.headOption.map(cnew => {
				cnew ! Network.NetworkUpdate(evalG, e)
				rep(g.tail, c.tail, cnew :: cummulate, innovation, envType)
			}).getOrElse(
				// we should actually mutate here too? If it's a new member it won't mutate otherwise, Since mutation is being done at network level
				// though not much effect if the population doesn't fluctuate that much.
				context.actorOf(Agent.props(innovation, evalG, configData, ActiveAgent, envType), "agent" + "weneedtospecanid") :: cummulate)
		}).getOrElse({
			//c.foreach(newc => context.stop(newc))
			c ++ cummulate
		})	
	}


 	

	startWith(Active, PopulationSettings(
		currentPopulationSize = configData.populationSize,
		speciesDirectory = SpeciesDirectory(configData = configData)
		))

	when(Active) {

		case Event(d: EnvData, s: PopulationSettings) =>
 			
 			// this should be controlled by data coming from EnvData... 
 			val envType: FileEnvironmentType = new FileEnvironmentType(d.data)


			val innovation = context.actorOf(Innovation.props(new GenomeIO(None, None).generateFromSpec(envType.environmentIOSpec)), "innov8")
 			val popList = for {
 			i <- 1 to configData.populationSize
 			}
 			yield {
 				context.actorOf(Agent.props(innovation,  new GenomeIO(None, None), configData, ActiveAgent, envType), "agent" + i)
			}

			log.info("population created with {} children", context.children.size) 

			val newSettings = s.copy(currentPopulation = popList.toList, innovation = innovation, envType = envType)

			stay using newSettings

 		case Event(d: Network.Done, s: PopulationSettings) =>
 			
 			val completed = s.agentsCompleteCount + 1
			
			// decide species.
			
			val (allocatedSpecies, newSpeciesDir) = s.speciesDirectory.allocate(d.genome, d.evaluator.fitness, d.evaluator.auxValue)
			
			val updateGenome = d.genome.copy(species= allocatedSpecies, generation = s.currentGeneration)

			val logParams = Array(s.currentGeneration, d.evaluator.fitness, d.evaluator.auxValue, updateGenome.toJson, sender(), completed, s.currentPopulationSize)

			val insertDBRecord = genomeCollection.flatMap(_.insert(BSONDocument(
				"runId" -> runId,
				"generation" -> s.currentGeneration,
				"species"-> allocatedSpecies,
				"fitness" -> d.evaluator.fitness,
				"genome" -> updateGenome
			)).map(_ => {}))

			log.info("generation {} population received fitness value of {}, performance Value of: {} for genome: {} from {}. received {} of {} ", logParams)
						


 			if(completed == s.currentPopulationSize) {
 				// so....  we have got n genomes, each with a Performance value of some sort....
 				// time to select the best in line with their performance and send the genome back to agent whi should forward on to the network
 				out ! ("{\"currentGeneration\": " + s.currentGeneration + ", \"totalGenerations\": " + generations + "}")
 				val speciesList = s.speciesDirectory.species.filter(s=> s._2.memberCount > 0)
 				val activeSpecies = speciesList.size

 				//log.debug("population: All Agents Completed")
 				val insertGenRecord = generationCollection.flatMap(_.insert(BSONDocument(
 							"runId" -> runId,
							"generation" -> s.currentGeneration,
							"currentPopulation" -> s.currentPopulationSize,
						    "activeSpecies" -> activeSpecies,
						 	"bestPerformance" -> s.speciesDirectory.bestMember.performanceValue,
						 	"bestFitness" -> s.speciesDirectory.bestMember.fitnessValue)))

 				val speciesForInsert = speciesList.values.foreach(species=> {
 					val d = speciesCollection.flatMap(_.insert(BSONDocument(
 					"runId" -> runId,
 					"generation" -> s.currentGeneration,
 					"species" -> species.id,
 					"memberCount" -> species.memberCount,
 					"speciesTotalFitness" -> species.speciesTotalFitness,
 					"speciesBestFitness" -> species.generationalArchetype.fitnessValue
 				)))})
 				

 				//,
				//		 "species" -> s.speciesDirectory.species.filter(s=> s._2.memberCount > 0).values)).map(_ => {})
 				
				
 				// check to see if we have done all generations

 				if (s.currentGeneration == generations) {
 					log.debug("All generations finished")
 					val endTime = Calendar.getInstance().getTimeInMillis()
 					val duration = (endTime - startTime) / 1000
 					out ! (runId + " finished in " + duration + " seconds")

					val insertDBRecord = runStatsCollection.flatMap(_.insert(BSONDocument(
						"envId" -> configData.environmentId,
						"runId" -> runId,
						"startTime" -> startTime,
						"settings" -> configData,
						"endTime" -> endTime,
						"duration" -> duration,
						"bestFitness" -> s.speciesDirectory.bestMember.fitnessValue,
						"bestPerformance" -> s.speciesDirectory.bestMember.performanceValue
					)).map(_ => {}))

 					context.stop(self)
 					stay
 				} else {



 				val gestatable = s.speciesDirectory.selectGenerationSurvivors
 				log.debug("gestatable. find a better name. : {}", gestatable)

 				// time to reset the species.
 				val resetSpeciesDir = s.speciesDirectory.reorder.reset
 				
 				// Now we have a load of functions to run we need to send them to available agents. 
 				//creating new ones if required and shutting down old ones..

 				
 				val agentFns = gestatable.flatten.toList
 				val pop = repurposeAgents(agentFns, s.currentPopulation, s.innovation, s.envType)

 				stay using s.copy(
 								  currentGeneration = s.currentGeneration + 1,
 								  currentPopulation = pop,
 								  currentPopulationSize = agentFns.length,
 								  agentsCompleteCount = 0, 	
 								  agentSumTotalFitness = 0,
 								  speciesDirectory = resetSpeciesDir
 								)
 				}
 			} else {	 
 			
 				stay using s.copy(agentsCompleteCount = completed, 	speciesDirectory = newSpeciesDir)
 			}
 	}
}