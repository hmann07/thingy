package com.thingy.agent

import com.thingy.config.ConfigDataClass.ConfigData
import akka.actor.{ ActorRef, FSM, Props }
import com.thingy.genome.{NetworkGenomeBuilder, NetworkGenome,GenomeIO}
import com.thingy.network.{Network, NetworkTest, NetworkActive}
import com.thingy.innovation._
import com.thingy.neuron.Neuron
import com.thingy.evaluator._
import com.thingy.environment.Environment
import com.thingy.environment.EnvironmentType
import com.thingy.environment.EnvironmentIOSpec
import akka.pattern.pipe
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.{ DefaultDB, MongoConnection, MongoDriver }
import reactivemongo.bson.{
  BSONWriter, BSONDocument, BSONDouble, BSONDocumentWriter, BSONDocumentReader, Macros, document
}
import play.api.libs.json._
import reactivemongo.play.json._
import com.thingy.population.Population.EnvData
import com.thingy.environment.FileEnvironmentType

sealed trait AgentState
case object Initialising extends AgentState
case object ActiveAgent extends AgentState
case object TestState extends AgentState
case object ConfigureState extends AgentState



object Agent {

	case class AgentSettings(runState: AgentState = null, network: ActorRef = null, evaluator: Evaluator = null, environment: ActorRef = null)
	case class Performance(performanceValue: Double, genome: NetworkGenome)
	
	def props(innovation: ActorRef, network: GenomeIO, configData: ConfigData, startState: AgentState, envType: EnvironmentType, out: ActorRef = null): Props = {
		
		Props(classOf[Agent], innovation, network, configData, startState, envType, out)
	}

}


// environmetn type will come null when making a test, so we'll need to get it from the database
class Agent(innovation: ActorRef, ng: GenomeIO, configData: ConfigData, startState: AgentState, envType: EnvironmentType, out: ActorRef = null) extends FSM[AgentState, Agent.AgentSettings] {
	import Agent._
	
	val (updatedSettings, state) = startState match {
		case TestState =>
			val mongoUri = "mongodb://localhost:27017/genomeData?authMode=scram-sha1"

		 	 // Connect to the database: Must be done only once per application
		  	val driver = MongoDriver()
		  	val parsedUri = MongoConnection.parseURI(mongoUri)
		  	val connection = parsedUri.map(driver.connection(_))
		  	
		  	// Database and collections: Get references
		  	val futureConnection = Future.fromTry(connection)
		  	def db1: Future[DefaultDB] = futureConnection.flatMap(_.database("genomeData"))
		  	def environmentCollection = db1.map(_.collection("environments"))
		  	
			val query = BSONDocument("_id" -> Json.obj("$oid" -> configData.environmentId))
			val envData = environmentCollection.flatMap(_.find(query).one[BSONDocument])
			
			def extractEnvData(implicit ec: ExecutionContext) = { 

				envData.map {d=>
					EnvData(d.get)
				}
			}


			extractEnvData.pipeTo(self)

			(AgentSettings(), ConfigureState)
	
		case ActiveAgent => 

			def networkGenome = ng match {
				case GenomeIO(None, None) => {
					val cg = ng.generateFromSpec(envType.environmentIOSpec)
					log.info("creating genome from spec " + cg)
					cg
				}
				case _ => ng.generate
			}
	 
			val environment = context.actorOf(Environment.props(envType), "environment")
			val evaluator = configData.evaluatorType match {
				case "SSEEvaluator" => SSEEvaluator(fieldMap = envType.environmentIOSpec)
				case "SoftmaxEvaluator" => SoftmaxEvaluator(fieldMap = envType.environmentIOSpec)

			}
			val net = context.actorOf(Network.props("my network", networkGenome, innovation, environment, configData, NetworkActive, evaluator, null), "mynetwork")
			(AgentSettings(runState = ActiveAgent, network = net, evaluator = evaluator, environment = environment), ActiveAgent)		
		}



 	startWith(state, updatedSettings)

 	when(ActiveAgent) {
 		case Event(g: Network.Mutated, t: AgentSettings) =>
 			log.debug("netowork finished mutating.")

 			stay

 		case Event(g: Network.Done, t: AgentSettings) =>
 			
 			context.parent ! g

 			stay

 		case Event(ng: Network.NetworkUpdate , s: AgentSettings) =>

 			log.debug("received network")
 			s.network ! ng
 			stay
 	}

 	when(TestState){

 		case Event(g: Network.Completed, t: AgentSettings) =>
 			
 			println("TestComplete")
 			//context.parent ! g
 			context.stop(self)
 			stay
 	}

 	when(ConfigureState){

 		case Event(d: EnvData, t: AgentSettings) =>

			val envType: FileEnvironmentType = new FileEnvironmentType(d.data)
			def networkGenome = ng match {
				case GenomeIO(None, None) => {
					
					val cg = ng.generateFromSpec(envType.environmentIOSpec)
					log.info("creating genome from spec " + cg)
					cg
				}
				case _ => ng.generate
			}

			val environment = context.actorOf(Environment.props(envType), "environment")
			val evaluator = SoftmaxEvaluator(fieldMap = envType.environmentIOSpec)
			val net = context.actorOf(Network.props("my network", networkGenome, innovation, environment, configData, NetworkTest, evaluator, out), "mynetwork")
			val updatedSettings = t.copy(runState= TestState, network = net, evaluator = evaluator, environment = environment)
			goto(TestState) using updatedSettings

 	}


 	

}
