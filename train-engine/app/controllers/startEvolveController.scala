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


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class StartEvolveController @Inject()(cache: SyncCacheApi, cc: ControllerComponents) (implicit assetsFinder: AssetsFinder, system: ActorSystem, mat: Materializer)
  extends AbstractController(cc) with play.api.i18n.I18nSupport {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def start() = Action {implicit request =>

   //  val p = system.actorOf(Props[Population], "population1")
   Ok(views.html.start(routes.AuthenticatedUserController.logout)).withSession(
  request.session + ("ME" -> "192.168.99.100"))
  }

  def startWS = WebSocket.accept[String, String] { request =>
    val futureMaybeUser: Option[ConfigData] = cache.get[ConfigData]("item.key")
      
      ActorFlow.actorRef { out =>
        Population.props(out, futureMaybeUser.get)
    }
  }
}
