package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.functional.syntax._
import play.api.libs.json._

import akka.actor._
import play.api.cache._
import scala.concurrent.Future


import play.api.libs.streams.ActorFlow

import akka.stream.Materializer

import com.thingy.population._
import com.thingy.config.ConfigDataClass.ConfigData
import java.nio.file.Paths

import reactivemongo.api.Cursor

import play.modules.reactivemongo.{ // ReactiveMongo Play2 plugin
  MongoController,
  ReactiveMongoApi,
  ReactiveMongoComponents

}

import reactivemongo.bson.BSONDocument

import reactivemongo.play.json._
import play.modules.reactivemongo.json.collection._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class EnvironmentController @Inject()(cache: SyncCacheApi, cc: ControllerComponents) (val reactiveMongoApi: ReactiveMongoApi) (implicit assetsFinder: AssetsFinder, system: ActorSystem, mat: Materializer)
  extends AbstractController(cc) with play.api.i18n.I18nSupport with MongoController with ReactiveMongoComponents {

  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("environments"))
  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def newEnvironment() = Action {implicit request =>

   //  val p = system.actorOf(Props[Population], "population1")
   Ok(views.html.setupenvironment(routes.AuthenticatedUserController.logout))
  }


  def createEnvironment =  Action.async(parse.multipartFormData) {implicit request =>
    println(request.body)
    val f = request.body.file("file").get

    // only get the last part of the filename
    // otherwise someone can send a path like ../../home/foo/bar.txt to write to other files on the system
    val filename = Paths.get(f.filename).getFileName
    
    f.ref.moveTo(Paths.get(s"c:\\Windows\\Temp\\$filename"), replace = true)
    //Ok("File uploaded")

    
    
    val envData = BSONDocument(
      "name" -> request.body.dataParts("environmentName").head,
      "fieldmap" -> request.body.dataParts("fieldmapping").head.split(","),
      "filename" -> filename.toString)
    
    val d =  collection.flatMap(_.insert(envData))
    d.map(_ => Ok("TODO create environment"))

    
  }


  def viewEnvironments = Action {implicit request =>
    Ok(views.html.viewEnvironments(routes.AuthenticatedUserController.logout))
  }
}
