package controllers

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.functional.syntax._
import play.api.libs.json._

import com.thingy.genome.NetworkGenome
import com.thingy.genome.GenomeIO
import javax.inject._

import play.api.mvc._

// Reactive Mongo imports
import reactivemongo.api.Cursor

import play.modules.reactivemongo.{ // ReactiveMongo Play2 plugin
  MongoController,
  ReactiveMongoApi,
  ReactiveMongoComponents

}

import reactivemongo.bson.BSONDocument

import reactivemongo.play.json._
import play.modules.reactivemongo.json.collection._


@Singleton
class MongoLookupController @Inject()(cc: ControllerComponents)(val reactiveMongoApi: ReactiveMongoApi) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents {

    
    def findBySpecies(species: String) = Action.async {
    // let's do our query
    val convSpecies = species.toInt

    def collection: JSONCollection = db.collection[JSONCollection]("genomes")
    
    val cursor: Cursor[JsObject] = collection.
      // find all people with name `name`
      find(Json.obj("species" -> convSpecies)).
      // sort them by creation date
      sort(Json.obj("generation" -> 1)).
      // perform the query and get a cursor of JsObject
      cursor[JsObject]()

    // gather all the JsObjects in a list
    val futureUsersList: Future[List[JsObject]] = cursor.collect[List]()

    // everything's ok! Let's reply with the array
    futureUsersList.map { persons =>
      Ok(Json.toJson(persons))
    }
  }

  def getSpeciesByGeneration(runId: String, generation: String) = Action.async {
    // let's do our query
    val convGen = generation.toInt
    //val convRun = runId.toInt

    def collection: JSONCollection = db.collection[JSONCollection]("species")
    
    val cursor: Cursor[JsObject] = collection.
      // find all people with name `name`
      find(Json.obj("generation" -> convGen, "runId" -> runId )).
      // sort them by creation date
      //sort(Json.obj("spec" -> 1)).
      // perform the query and get a cursor of JsObject
      cursor[JsObject]()

    // gather all the JsObjects in a list
    val futureUsersList: Future[List[JsObject]] = cursor.collect[List]()

    // everything's ok! Let's reply with the array
    futureUsersList.map { persons =>
      Ok(Json.toJson(persons))
    }
  }

  def findBySpeciesandGen (runId: String, generation: String, species: String) = Action.async {
    // let's do our query
    val convGen = generation.toInt
    val convSpec = species.toInt

    def collection: JSONCollection = db.collection[JSONCollection]("genomes")
    
    val cursor: Cursor[JsObject] = collection.
      // find all people with name `name`
      find(Json.obj("runId"-> runId, "generation" -> convGen, "species" -> convSpec )).
      // sort them by creation date
      //sort(Json.obj("spec" -> 1)).convSpec
      // perform the query and get a cursor of JsObject
      cursor[JsObject]()

    // gather all the JsObjects in a list
    val futureUsersList: Future[List[JsObject]] = cursor.collect[List]()

    // everything's ok! Let's reply with the array
    futureUsersList.map { persons =>
      Ok(Json.toJson(persons))
    }
  }



  def getGenerations = Action.async {
    // let's do our query
    def collection: JSONCollection = db.collection[JSONCollection]("generations")
    
    val cursor: Cursor[JsObject] = collection.
      // find all people with name `name`
      find(Json.obj()).
      // sort them by creation date
      //sort(Json.obj("generation" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[JsObject]()

    // gather all the JsObjects in a list
    val futureUsersList: Future[List[JsObject]] = cursor.collect[List]()

    // everything's ok! Let's reply with the array
    futureUsersList.map { persons =>
      Ok(Json.toJson(persons))
    }
  }

  def getRuns = Action.async {
    // let's do our query
    def collection: JSONCollection = db.collection[JSONCollection]("runs")
    
    val cursor: Cursor[JsObject] = collection.
      // find all people with name `name`
      find(Json.obj()).
      // sort them by creation date
      //sort(Json.obj("generation" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[JsObject]()

    // gather all the JsObjects in a list
    val futureUsersList: Future[List[JsObject]] = cursor.collect[List]()

    // everything's ok! Let's reply with the array
    futureUsersList.map { run =>
      Ok(Json.toJson(run))
    }
  }


  def getRunsByEnv(envId: String) = Action.async {

     def collection: JSONCollection = db.collection[JSONCollection]("runs")
      
      val cursor: Cursor[JsObject] = collection.
        // find all people with name `name`
        find(Json.obj("envId" -> envId)).
        // sort them by creation date
        //sort(Json.obj("generation" -> -1)).
        // perform the query and get a cursor of JsObject
        cursor[JsObject]()

      // gather all the JsObjects in a list
      val futureRunsList: Future[List[JsObject]] = cursor.collect[List]()

      // everything's ok! Let's reply with the array
      futureRunsList.map { run =>
        Ok(Json.toJson(run))
      } 
  }



  def getEnvironments = Action.async {
    // let's do our query
    def collection: JSONCollection = db.collection[JSONCollection]("environments")
    
    val cursor: Cursor[JsObject] = collection.
      // find all people with name `name`
      find(Json.obj()).
      // sort them by creation date
      //sort(Json.obj("generation" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[JsObject]()

    // gather all the JsObjects in a list
    val futureEnvironments: Future[List[JsObject]] = cursor.collect[List]()

    // everything's ok! Let's reply with the array
    futureEnvironments.map { run =>
      Ok(Json.toJson(run))
    }
  }



  def getGenerationsByRun(runId: String) = Action.async {
    // let's do our query
    val convRun = runId

    def collection: JSONCollection = db.collection[JSONCollection]("generations")
    
    val cursor: Cursor[JsObject] = collection.
      // find all people with name `name`
      find(Json.obj("runId" -> convRun)).
      // sort them by creation date
      //sort(Json.obj("spec" -> 1)).
      // perform the query and get a cursor of JsObject
      cursor[JsObject]()

    // gather all the JsObjects in a list
    val futureGenList: Future[List[JsObject]] = cursor.collect[List]()

    // everything's ok! Let's reply with the array
    futureGenList.map { gens =>
      Ok(Json.toJson(gens))
    }
  }

  def index = Action {
    Ok("working")
  }

}
