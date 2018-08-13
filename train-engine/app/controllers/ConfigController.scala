package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.functional.syntax._
import play.api.libs.json._

import com.thingy.genome.NetworkGenome
import com.thingy.genome.GenomeIO
import com.thingy.config.ConfigDataClass.ConfigData
import javax.inject._
import play.api.cache._
import scala.concurrent.Future

import play.api.mvc._
import akka.Done

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

/*
seed-network = "/thingy/app/thingySrc/resources/seed.json"
  seed-subnet = "/thingy/app/thingySrc/resources/subnet-seed.json"
  
  
  species-starting-threshold = 2
  species-distance-weight-factor = 0.3
  crossover-rate = 0.75
  mutation-rate = 0.95
  weight-mutation-likelihood = 0.85 
  jiggle-over-reset=.90
  mutation-weights {
    network-connection = 0.5
    sub-network-connection = 0.5
  }
*/

@Singleton
class ConfigController @Inject()(cache: SyncCacheApi, cc: ControllerComponents)(val reactiveMongoApi: ReactiveMongoApi) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents with play.api.i18n.I18nSupport {

    
    
  import play.api.data.Form
  import play.api.data.Forms._
 import play.api.data.format.Formats._
  

  val configForm = Form(
    mapping(
      "populationSize" -> number,
      "maxGenerations" -> number,
      "connectionWeightRange" -> number,
      "speciesMembershipThreshold" -> of(doubleFormat),
      "crossoverRate" -> of(doubleFormat),
      "globalMutationRate" -> of(doubleFormat),
      "weightMutationRate" -> of(doubleFormat),
      "weightJiggleOverReset" -> of(doubleFormat)
    )(ConfigData.apply)(ConfigData.unapply)
  )

  implicit val configReads = Json.reads[ConfigData]

  def viewConfig = Action { implicit request =>
    Ok(views.html.config(configForm.fill(ConfigData()), routes.AuthenticatedUserController.logout))
  }

  def viewConfigFilled = Action { implicit request =>
    val retrievedForm = cache.get[ConfigData]("config.form").get
    Ok(views.html.config(configForm.fill(retrievedForm), routes.AuthenticatedUserController.logout))
  }


  def reuseConfig = Action { implicit request =>
 
    val extractedConfig = configForm.bindFromRequest.get
    println(extractedConfig)
    val savedForm = cache.set("config.form", extractedConfig)
    Redirect(routes.ConfigController.viewConfigFilled)
  }

  def userPost = Action { implicit request =>

    val test: ConfigData = configForm.bindFromRequest.get
    val result = cache.set("item.key", test)
    Redirect(routes.StartEvolveController.start)
  }


}
