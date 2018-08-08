// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/train-engine/conf/routes
// @DATE:Mon Jul 23 23:01:17 BST 2018

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  LandingPageController_6: controllers.LandingPageController,
  // @LINE:8
  NetViewerController_7: controllers.NetViewerController,
  // @LINE:10
  MongoLookupController_5: controllers.MongoLookupController,
  // @LINE:14
  ConfigController_3: controllers.ConfigController,
  // @LINE:26
  StartEvolveController_0: controllers.StartEvolveController,
  // @LINE:31
  Assets_4: controllers.Assets,
  // @LINE:35
  UserController_1: controllers.UserController,
  // @LINE:39
  AuthenticatedUserController_2: controllers.AuthenticatedUserController,
  // @LINE:43
  webjars_Routes_0: webjars.Routes,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    LandingPageController_6: controllers.LandingPageController,
    // @LINE:8
    NetViewerController_7: controllers.NetViewerController,
    // @LINE:10
    MongoLookupController_5: controllers.MongoLookupController,
    // @LINE:14
    ConfigController_3: controllers.ConfigController,
    // @LINE:26
    StartEvolveController_0: controllers.StartEvolveController,
    // @LINE:31
    Assets_4: controllers.Assets,
    // @LINE:35
    UserController_1: controllers.UserController,
    // @LINE:39
    AuthenticatedUserController_2: controllers.AuthenticatedUserController,
    // @LINE:43
    webjars_Routes_0: webjars.Routes
  ) = this(errorHandler, LandingPageController_6, NetViewerController_7, MongoLookupController_5, ConfigController_3, StartEvolveController_0, Assets_4, UserController_1, AuthenticatedUserController_2, webjars_Routes_0, "/")

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, LandingPageController_6, NetViewerController_7, MongoLookupController_5, ConfigController_3, StartEvolveController_0, Assets_4, UserController_1, AuthenticatedUserController_2, webjars_Routes_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.LandingPageController.showLandingPage"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """netviewer""", """controllers.NetViewerController.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """mongolookup/""" + "$" + """speciesId<[^/]+>""", """controllers.MongoLookupController.findBySpecies(speciesId:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """getSpeciesByGeneration/""" + "$" + """runId<[^/]+>/""" + "$" + """generationId<[^/]+>""", """controllers.MongoLookupController.getSpeciesByGeneration(runId:String, generationId:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """config""", """controllers.ConfigController.viewConfig"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """userPost""", """controllers.ConfigController.userPost"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """getNetByGenerationAndSpecies/""" + "$" + """runId<[^/]+>/""" + "$" + """generationId<[^/]+>/""" + "$" + """speciesId<[^/]+>""", """controllers.MongoLookupController.findBySpeciesandGen(runId:String, generationId:String, speciesId:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """generations""", """controllers.MongoLookupController.getGenerations"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """generations/""" + "$" + """runId<[^/]+>""", """controllers.MongoLookupController.getGenerationsByRun(runId:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """runs""", """controllers.MongoLookupController.getRuns"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """start""", """controllers.StartEvolveController.start"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """startWS""", """controllers.StartEvolveController.startWS"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(file:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """users/login""", """controllers.UserController.showLoginForm"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """users/doLogin""", """controllers.UserController.processLoginAttempt"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """users/logout""", """controllers.AuthenticatedUserController.logout"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """landingPage""", """controllers.LandingPageController.showLandingPage"""),
    prefixed_webjars_Routes_0_17.router.documentation,
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:6
  private[this] lazy val controllers_LandingPageController_showLandingPage0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_LandingPageController_showLandingPage0_invoker = createInvoker(
    LandingPageController_6.showLandingPage,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.LandingPageController",
      "showLandingPage",
      Nil,
      "GET",
      this.prefix + """""",
      """ An example controller showing a sample home page""",
      Seq()
    )
  )

  // @LINE:8
  private[this] lazy val controllers_NetViewerController_index1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("netviewer")))
  )
  private[this] lazy val controllers_NetViewerController_index1_invoker = createInvoker(
    NetViewerController_7.index,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.NetViewerController",
      "index",
      Nil,
      "GET",
      this.prefix + """netviewer""",
      """ the net viewer""",
      Seq()
    )
  )

  // @LINE:10
  private[this] lazy val controllers_MongoLookupController_findBySpecies2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("mongolookup/"), DynamicPart("speciesId", """[^/]+""",true)))
  )
  private[this] lazy val controllers_MongoLookupController_findBySpecies2_invoker = createInvoker(
    MongoLookupController_5.findBySpecies(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.MongoLookupController",
      "findBySpecies",
      Seq(classOf[String]),
      "GET",
      this.prefix + """mongolookup/""" + "$" + """speciesId<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:12
  private[this] lazy val controllers_MongoLookupController_getSpeciesByGeneration3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("getSpeciesByGeneration/"), DynamicPart("runId", """[^/]+""",true), StaticPart("/"), DynamicPart("generationId", """[^/]+""",true)))
  )
  private[this] lazy val controllers_MongoLookupController_getSpeciesByGeneration3_invoker = createInvoker(
    MongoLookupController_5.getSpeciesByGeneration(fakeValue[String], fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.MongoLookupController",
      "getSpeciesByGeneration",
      Seq(classOf[String], classOf[String]),
      "GET",
      this.prefix + """getSpeciesByGeneration/""" + "$" + """runId<[^/]+>/""" + "$" + """generationId<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:14
  private[this] lazy val controllers_ConfigController_viewConfig4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("config")))
  )
  private[this] lazy val controllers_ConfigController_viewConfig4_invoker = createInvoker(
    ConfigController_3.viewConfig,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ConfigController",
      "viewConfig",
      Nil,
      "GET",
      this.prefix + """config""",
      """""",
      Seq()
    )
  )

  // @LINE:16
  private[this] lazy val controllers_ConfigController_userPost5_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("userPost")))
  )
  private[this] lazy val controllers_ConfigController_userPost5_invoker = createInvoker(
    ConfigController_3.userPost,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ConfigController",
      "userPost",
      Nil,
      "POST",
      this.prefix + """userPost""",
      """""",
      Seq()
    )
  )

  // @LINE:18
  private[this] lazy val controllers_MongoLookupController_findBySpeciesandGen6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("getNetByGenerationAndSpecies/"), DynamicPart("runId", """[^/]+""",true), StaticPart("/"), DynamicPart("generationId", """[^/]+""",true), StaticPart("/"), DynamicPart("speciesId", """[^/]+""",true)))
  )
  private[this] lazy val controllers_MongoLookupController_findBySpeciesandGen6_invoker = createInvoker(
    MongoLookupController_5.findBySpeciesandGen(fakeValue[String], fakeValue[String], fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.MongoLookupController",
      "findBySpeciesandGen",
      Seq(classOf[String], classOf[String], classOf[String]),
      "GET",
      this.prefix + """getNetByGenerationAndSpecies/""" + "$" + """runId<[^/]+>/""" + "$" + """generationId<[^/]+>/""" + "$" + """speciesId<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:20
  private[this] lazy val controllers_MongoLookupController_getGenerations7_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("generations")))
  )
  private[this] lazy val controllers_MongoLookupController_getGenerations7_invoker = createInvoker(
    MongoLookupController_5.getGenerations,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.MongoLookupController",
      "getGenerations",
      Nil,
      "GET",
      this.prefix + """generations""",
      """""",
      Seq()
    )
  )

  // @LINE:22
  private[this] lazy val controllers_MongoLookupController_getGenerationsByRun8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("generations/"), DynamicPart("runId", """[^/]+""",true)))
  )
  private[this] lazy val controllers_MongoLookupController_getGenerationsByRun8_invoker = createInvoker(
    MongoLookupController_5.getGenerationsByRun(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.MongoLookupController",
      "getGenerationsByRun",
      Seq(classOf[String]),
      "GET",
      this.prefix + """generations/""" + "$" + """runId<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:24
  private[this] lazy val controllers_MongoLookupController_getRuns9_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("runs")))
  )
  private[this] lazy val controllers_MongoLookupController_getRuns9_invoker = createInvoker(
    MongoLookupController_5.getRuns,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.MongoLookupController",
      "getRuns",
      Nil,
      "GET",
      this.prefix + """runs""",
      """""",
      Seq()
    )
  )

  // @LINE:26
  private[this] lazy val controllers_StartEvolveController_start10_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("start")))
  )
  private[this] lazy val controllers_StartEvolveController_start10_invoker = createInvoker(
    StartEvolveController_0.start,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.StartEvolveController",
      "start",
      Nil,
      "GET",
      this.prefix + """start""",
      """""",
      Seq()
    )
  )

  // @LINE:28
  private[this] lazy val controllers_StartEvolveController_startWS11_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("startWS")))
  )
  private[this] lazy val controllers_StartEvolveController_startWS11_invoker = createInvoker(
    StartEvolveController_0.startWS,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.StartEvolveController",
      "startWS",
      Nil,
      "GET",
      this.prefix + """startWS""",
      """""",
      Seq()
    )
  )

  // @LINE:31
  private[this] lazy val controllers_Assets_versioned12_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned12_invoker = createInvoker(
    Assets_4.versioned(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """ Map static resources from the /public folder to the /assets URL path""",
      Seq()
    )
  )

  // @LINE:35
  private[this] lazy val controllers_UserController_showLoginForm13_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/login")))
  )
  private[this] lazy val controllers_UserController_showLoginForm13_invoker = createInvoker(
    UserController_1.showLoginForm,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "showLoginForm",
      Nil,
      "GET",
      this.prefix + """users/login""",
      """ user/admin stuff""",
      Seq()
    )
  )

  // @LINE:36
  private[this] lazy val controllers_UserController_processLoginAttempt14_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/doLogin")))
  )
  private[this] lazy val controllers_UserController_processLoginAttempt14_invoker = createInvoker(
    UserController_1.processLoginAttempt,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.UserController",
      "processLoginAttempt",
      Nil,
      "POST",
      this.prefix + """users/doLogin""",
      """""",
      Seq()
    )
  )

  // @LINE:39
  private[this] lazy val controllers_AuthenticatedUserController_logout15_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/logout")))
  )
  private[this] lazy val controllers_AuthenticatedUserController_logout15_invoker = createInvoker(
    AuthenticatedUserController_2.logout,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.AuthenticatedUserController",
      "logout",
      Nil,
      "GET",
      this.prefix + """users/logout""",
      """ pages for authenticated users""",
      Seq()
    )
  )

  // @LINE:40
  private[this] lazy val controllers_LandingPageController_showLandingPage16_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("landingPage")))
  )
  private[this] lazy val controllers_LandingPageController_showLandingPage16_invoker = createInvoker(
    LandingPageController_6.showLandingPage,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.LandingPageController",
      "showLandingPage",
      Nil,
      "GET",
      this.prefix + """landingPage""",
      """""",
      Seq()
    )
  )

  // @LINE:43
  private[this] val prefixed_webjars_Routes_0_17 = Include(webjars_Routes_0.withPrefix(this.prefix + (if (this.prefix.endsWith("/")) "" else "/") + "webjars"))


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_LandingPageController_showLandingPage0_route(params@_) =>
      call { 
        controllers_LandingPageController_showLandingPage0_invoker.call(LandingPageController_6.showLandingPage)
      }
  
    // @LINE:8
    case controllers_NetViewerController_index1_route(params@_) =>
      call { 
        controllers_NetViewerController_index1_invoker.call(NetViewerController_7.index)
      }
  
    // @LINE:10
    case controllers_MongoLookupController_findBySpecies2_route(params@_) =>
      call(params.fromPath[String]("speciesId", None)) { (speciesId) =>
        controllers_MongoLookupController_findBySpecies2_invoker.call(MongoLookupController_5.findBySpecies(speciesId))
      }
  
    // @LINE:12
    case controllers_MongoLookupController_getSpeciesByGeneration3_route(params@_) =>
      call(params.fromPath[String]("runId", None), params.fromPath[String]("generationId", None)) { (runId, generationId) =>
        controllers_MongoLookupController_getSpeciesByGeneration3_invoker.call(MongoLookupController_5.getSpeciesByGeneration(runId, generationId))
      }
  
    // @LINE:14
    case controllers_ConfigController_viewConfig4_route(params@_) =>
      call { 
        controllers_ConfigController_viewConfig4_invoker.call(ConfigController_3.viewConfig)
      }
  
    // @LINE:16
    case controllers_ConfigController_userPost5_route(params@_) =>
      call { 
        controllers_ConfigController_userPost5_invoker.call(ConfigController_3.userPost)
      }
  
    // @LINE:18
    case controllers_MongoLookupController_findBySpeciesandGen6_route(params@_) =>
      call(params.fromPath[String]("runId", None), params.fromPath[String]("generationId", None), params.fromPath[String]("speciesId", None)) { (runId, generationId, speciesId) =>
        controllers_MongoLookupController_findBySpeciesandGen6_invoker.call(MongoLookupController_5.findBySpeciesandGen(runId, generationId, speciesId))
      }
  
    // @LINE:20
    case controllers_MongoLookupController_getGenerations7_route(params@_) =>
      call { 
        controllers_MongoLookupController_getGenerations7_invoker.call(MongoLookupController_5.getGenerations)
      }
  
    // @LINE:22
    case controllers_MongoLookupController_getGenerationsByRun8_route(params@_) =>
      call(params.fromPath[String]("runId", None)) { (runId) =>
        controllers_MongoLookupController_getGenerationsByRun8_invoker.call(MongoLookupController_5.getGenerationsByRun(runId))
      }
  
    // @LINE:24
    case controllers_MongoLookupController_getRuns9_route(params@_) =>
      call { 
        controllers_MongoLookupController_getRuns9_invoker.call(MongoLookupController_5.getRuns)
      }
  
    // @LINE:26
    case controllers_StartEvolveController_start10_route(params@_) =>
      call { 
        controllers_StartEvolveController_start10_invoker.call(StartEvolveController_0.start)
      }
  
    // @LINE:28
    case controllers_StartEvolveController_startWS11_route(params@_) =>
      call { 
        controllers_StartEvolveController_startWS11_invoker.call(StartEvolveController_0.startWS)
      }
  
    // @LINE:31
    case controllers_Assets_versioned12_route(params@_) =>
      call(params.fromPath[String]("file", None)) { (file) =>
        controllers_Assets_versioned12_invoker.call(Assets_4.versioned(file))
      }
  
    // @LINE:35
    case controllers_UserController_showLoginForm13_route(params@_) =>
      call { 
        controllers_UserController_showLoginForm13_invoker.call(UserController_1.showLoginForm)
      }
  
    // @LINE:36
    case controllers_UserController_processLoginAttempt14_route(params@_) =>
      call { 
        controllers_UserController_processLoginAttempt14_invoker.call(UserController_1.processLoginAttempt)
      }
  
    // @LINE:39
    case controllers_AuthenticatedUserController_logout15_route(params@_) =>
      call { 
        controllers_AuthenticatedUserController_logout15_invoker.call(AuthenticatedUserController_2.logout)
      }
  
    // @LINE:40
    case controllers_LandingPageController_showLandingPage16_route(params@_) =>
      call { 
        controllers_LandingPageController_showLandingPage16_invoker.call(LandingPageController_6.showLandingPage)
      }
  
    // @LINE:43
    case prefixed_webjars_Routes_0_17(handler) => handler
  }
}
