package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import akka.actor._
import play.api.cache._
import scala.concurrent.Future


import play.api.libs.streams.ActorFlow

import akka.stream.Materializer

import com.thingy.population._
import com.thingy.config.ConfigDataClass.ConfigData
import com.thingy.experiencestream.StreamFromFile
import com.thingy.genome._

import play.api.libs.json._
import com.thingy.config.ConfigDataClass.ConfigData
import com.thingy.agent._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class StartTestController @Inject()(cache: SyncCacheApi, cc: ControllerComponents) (implicit assetsFinder: AssetsFinder, system: ActorSystem, mat: Materializer)
  extends AbstractController(cc) with play.api.i18n.I18nSupport {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def submitGenome() = Action {implicit request =>

    println(request.body)
    val body: AnyContent = request.body
    val jsonBody: Option[JsValue] = Some(Json.parse(body.asFormUrlEncoded.get("genome").head))
    val envId = body.asFormUrlEncoded.get("envId").head

    // Expecting json body
    jsonBody.map { json =>
           
      val genome = (json \ "genome").validate[NetworkGenome]
      
      val t = new StreamFromFile()
      t.startStream

      println(genome)


      val agent = genome.map { g => 
          
        cache.set("test.genome", g)
        cache.set("test.envId", envId)
        
      }

      Ok(views.html.test(routes.AuthenticatedUserController.logout))
    
    }.getOrElse {
    
      BadRequest("Expecting application/json request body")
    
    }
  }


  def startWS() = WebSocket.accept[String, String] { request =>
    val maybeGenome: Option[NetworkGenome] = cache.get[NetworkGenome]("test.genome")
    val maybeEnvId: Option[String] = cache.get[String]("test.envId")
      
      ActorFlow.actorRef { out =>
        Agent.props(null,  new GenomeIO(None, Some(()=> maybeGenome.get)), ConfigData(environmentId = maybeEnvId.get), TestState, List.empty, out)
    }
  }

  //def startWS = WebSocket.accept[String, String] { request =>
  //  val futureMaybeUser: Option[ConfigData] = cache.get[ConfigData]("item.key")
      
  //    ActorFlow.actorRef { out =>
 //       Population.props(out, futureMaybeUser.get)
  //  }
  //}
}
