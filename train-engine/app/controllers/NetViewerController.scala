package controllers

import javax.inject._

import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class NetViewerController @Inject()(cc: ControllerComponents, authenticatedUserAction: AuthenticatedUserAction) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

   private val logoutUrl = routes.AuthenticatedUserController.logout

  def index = authenticatedUserAction { implicit request =>
    Ok(views.html.netViewer("test", logoutUrl))
  }

}
