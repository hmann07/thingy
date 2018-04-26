package controllers

import javax.inject._

import play.api.mvc._

import akka.actor._

import com.thingy.population._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class StartEvolveController @Inject()(system: ActorSystem, cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def start = Action {

     val p = system.actorOf(Props[Population], "population1")


    Ok("evolution has started")
  }

}
