// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/train-engine/conf/routes
// @DATE:Sun Aug 12 19:41:21 BST 2018

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  LandingPageController_7: controllers.LandingPageController,
  // @LINE:8
  NetViewerController_8: controllers.NetViewerController,
  // @LINE:10
  MongoLookupController_6: controllers.MongoLookupController,
  // @LINE:14
  ConfigController_4: controllers.ConfigController,
  // @LINE:16
  AggregateAnalysisController_3: controllers.AggregateAnalysisController,
  // @LINE:32
  StartEvolveController_0: controllers.StartEvolveController,
  // @LINE:37
  Assets_5: controllers.Assets,
  // @LINE:41
  UserController_1: controllers.UserController,
  // @LINE:45
  AuthenticatedUserController_2: controllers.AuthenticatedUserController,
  // @LINE:49
  webjars_Routes_0: webjars.Routes,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    LandingPageController_7: controllers.LandingPageController,
    // @LINE:8
    NetViewerController_8: controllers.NetViewerController,
    // @LINE:10
    MongoLookupController_6: controllers.MongoLookupController,
    // @LINE:14
    ConfigController_4: controllers.ConfigController,
    // @LINE:16
    AggregateAnalysisController_3: controllers.AggregateAnalysisController,
    // @LINE:32
    StartEvolveController_0: controllers.StartEvolveController,
    // @LINE:37
    Assets_5: controllers.Assets,
    // @LINE:41
    UserController_1: controllers.UserController,
    // @LINE:45
    AuthenticatedUserController_2: controllers.AuthenticatedUserController,
    // @LINE:49
    webjars_Routes_0: webjars.Routes
  ) = this(errorHandler, LandingPageController_7, NetViewerController_8, MongoLookupController_6, ConfigController_4, AggregateAnalysisController_3, StartEvolveController_0, Assets_5, UserController_1, AuthenticatedUserController_2, webjars_Routes_0, "/")

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, LandingPageController_7, NetViewerController_8, MongoLookupController_6, ConfigController_4, AggregateAnalysisController_3, StartEvolveController_0, Assets_5, UserController_1, AuthenticatedUserController_2, webjars_Routes_0, prefix)
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
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """aggregateAnalysis""", """controllers.AggregateAnalysisController.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """configFilled""", """controllers.ConfigController.viewConfigFilled"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """config/reuseConfig""", """controllers.ConfigController.reuseConfig"""),
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
    prefixed_webjars_Routes_0_20.router.documentation,
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
    LandingPageController_7.showLandingPage,
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
    NetViewerController_8.index,
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
    MongoLookupController_6.findBySpecies(fakeValue[String]),
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
    MongoLookupController_6.getSpeciesByGeneration(fakeValue[String], fakeValue[String]),
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
    ConfigController_4.viewConfig,
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
  private[this] lazy val controllers_AggregateAnalysisController_index5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("aggregateAnalysis")))
  )
  private[this] lazy val controllers_AggregateAnalysisController_index5_invoker = createInvoker(
    AggregateAnalysisController_3.index,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.AggregateAnalysisController",
      "index",
      Nil,
      "GET",
      this.prefix + """aggregateAnalysis""",
      """""",
      Seq()
    )
  )

  // @LINE:18
  private[this] lazy val controllers_ConfigController_viewConfigFilled6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("configFilled")))
  )
  private[this] lazy val controllers_ConfigController_viewConfigFilled6_invoker = createInvoker(
    ConfigController_4.viewConfigFilled,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ConfigController",
      "viewConfigFilled",
      Nil,
      "GET",
      this.prefix + """configFilled""",
      """""",
      Seq()
    )
  )

  // @LINE:20
  private[this] lazy val controllers_ConfigController_reuseConfig7_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("config/reuseConfig")))
  )
  private[this] lazy val controllers_ConfigController_reuseConfig7_invoker = createInvoker(
    ConfigController_4.reuseConfig,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ConfigController",
      "reuseConfig",
      Nil,
      "POST",
      this.prefix + """config/reuseConfig""",
      """""",
      Seq()
    )
  )

  // @LINE:22
  private[this] lazy val controllers_ConfigController_userPost8_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("userPost")))
  )
  private[this] lazy val controllers_ConfigController_userPost8_invoker = createInvoker(
    ConfigController_4.userPost,
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

  // @LINE:24
  private[this] lazy val controllers_MongoLookupController_findBySpeciesandGen9_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("getNetByGenerationAndSpecies/"), DynamicPart("runId", """[^/]+""",true), StaticPart("/"), DynamicPart("generationId", """[^/]+""",true), StaticPart("/"), DynamicPart("speciesId", """[^/]+""",true)))
  )
  private[this] lazy val controllers_MongoLookupController_findBySpeciesandGen9_invoker = createInvoker(
    MongoLookupController_6.findBySpeciesandGen(fakeValue[String], fakeValue[String], fakeValue[String]),
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

  // @LINE:26
  private[this] lazy val controllers_MongoLookupController_getGenerations10_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("generations")))
  )
  private[this] lazy val controllers_MongoLookupController_getGenerations10_invoker = createInvoker(
    MongoLookupController_6.getGenerations,
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

  // @LINE:28
  private[this] lazy val controllers_MongoLookupController_getGenerationsByRun11_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("generations/"), DynamicPart("runId", """[^/]+""",true)))
  )
  private[this] lazy val controllers_MongoLookupController_getGenerationsByRun11_invoker = createInvoker(
    MongoLookupController_6.getGenerationsByRun(fakeValue[String]),
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

  // @LINE:30
  private[this] lazy val controllers_MongoLookupController_getRuns12_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("runs")))
  )
  private[this] lazy val controllers_MongoLookupController_getRuns12_invoker = createInvoker(
    MongoLookupController_6.getRuns,
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

  // @LINE:32
  private[this] lazy val controllers_StartEvolveController_start13_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("start")))
  )
  private[this] lazy val controllers_StartEvolveController_start13_invoker = createInvoker(
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

  // @LINE:34
  private[this] lazy val controllers_StartEvolveController_startWS14_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("startWS")))
  )
  private[this] lazy val controllers_StartEvolveController_startWS14_invoker = createInvoker(
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

  // @LINE:37
  private[this] lazy val controllers_Assets_versioned15_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned15_invoker = createInvoker(
    Assets_5.versioned(fakeValue[String]),
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

  // @LINE:41
  private[this] lazy val controllers_UserController_showLoginForm16_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/login")))
  )
  private[this] lazy val controllers_UserController_showLoginForm16_invoker = createInvoker(
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

  // @LINE:42
  private[this] lazy val controllers_UserController_processLoginAttempt17_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/doLogin")))
  )
  private[this] lazy val controllers_UserController_processLoginAttempt17_invoker = createInvoker(
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

  // @LINE:45
  private[this] lazy val controllers_AuthenticatedUserController_logout18_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/logout")))
  )
  private[this] lazy val controllers_AuthenticatedUserController_logout18_invoker = createInvoker(
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

  // @LINE:46
  private[this] lazy val controllers_LandingPageController_showLandingPage19_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("landingPage")))
  )
  private[this] lazy val controllers_LandingPageController_showLandingPage19_invoker = createInvoker(
    LandingPageController_7.showLandingPage,
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

  // @LINE:49
  private[this] val prefixed_webjars_Routes_0_20 = Include(webjars_Routes_0.withPrefix(this.prefix + (if (this.prefix.endsWith("/")) "" else "/") + "webjars"))


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_LandingPageController_showLandingPage0_route(params@_) =>
      call { 
        controllers_LandingPageController_showLandingPage0_invoker.call(LandingPageController_7.showLandingPage)
      }
  
    // @LINE:8
    case controllers_NetViewerController_index1_route(params@_) =>
      call { 
        controllers_NetViewerController_index1_invoker.call(NetViewerController_8.index)
      }
  
    // @LINE:10
    case controllers_MongoLookupController_findBySpecies2_route(params@_) =>
      call(params.fromPath[String]("speciesId", None)) { (speciesId) =>
        controllers_MongoLookupController_findBySpecies2_invoker.call(MongoLookupController_6.findBySpecies(speciesId))
      }
  
    // @LINE:12
    case controllers_MongoLookupController_getSpeciesByGeneration3_route(params@_) =>
      call(params.fromPath[String]("runId", None), params.fromPath[String]("generationId", None)) { (runId, generationId) =>
        controllers_MongoLookupController_getSpeciesByGeneration3_invoker.call(MongoLookupController_6.getSpeciesByGeneration(runId, generationId))
      }
  
    // @LINE:14
    case controllers_ConfigController_viewConfig4_route(params@_) =>
      call { 
        controllers_ConfigController_viewConfig4_invoker.call(ConfigController_4.viewConfig)
      }
  
    // @LINE:16
    case controllers_AggregateAnalysisController_index5_route(params@_) =>
      call { 
        controllers_AggregateAnalysisController_index5_invoker.call(AggregateAnalysisController_3.index)
      }
  
    // @LINE:18
    case controllers_ConfigController_viewConfigFilled6_route(params@_) =>
      call { 
        controllers_ConfigController_viewConfigFilled6_invoker.call(ConfigController_4.viewConfigFilled)
      }
  
    // @LINE:20
    case controllers_ConfigController_reuseConfig7_route(params@_) =>
      call { 
        controllers_ConfigController_reuseConfig7_invoker.call(ConfigController_4.reuseConfig)
      }
  
    // @LINE:22
    case controllers_ConfigController_userPost8_route(params@_) =>
      call { 
        controllers_ConfigController_userPost8_invoker.call(ConfigController_4.userPost)
      }
  
    // @LINE:24
    case controllers_MongoLookupController_findBySpeciesandGen9_route(params@_) =>
      call(params.fromPath[String]("runId", None), params.fromPath[String]("generationId", None), params.fromPath[String]("speciesId", None)) { (runId, generationId, speciesId) =>
        controllers_MongoLookupController_findBySpeciesandGen9_invoker.call(MongoLookupController_6.findBySpeciesandGen(runId, generationId, speciesId))
      }
  
    // @LINE:26
    case controllers_MongoLookupController_getGenerations10_route(params@_) =>
      call { 
        controllers_MongoLookupController_getGenerations10_invoker.call(MongoLookupController_6.getGenerations)
      }
  
    // @LINE:28
    case controllers_MongoLookupController_getGenerationsByRun11_route(params@_) =>
      call(params.fromPath[String]("runId", None)) { (runId) =>
        controllers_MongoLookupController_getGenerationsByRun11_invoker.call(MongoLookupController_6.getGenerationsByRun(runId))
      }
  
    // @LINE:30
    case controllers_MongoLookupController_getRuns12_route(params@_) =>
      call { 
        controllers_MongoLookupController_getRuns12_invoker.call(MongoLookupController_6.getRuns)
      }
  
    // @LINE:32
    case controllers_StartEvolveController_start13_route(params@_) =>
      call { 
        controllers_StartEvolveController_start13_invoker.call(StartEvolveController_0.start)
      }
  
    // @LINE:34
    case controllers_StartEvolveController_startWS14_route(params@_) =>
      call { 
        controllers_StartEvolveController_startWS14_invoker.call(StartEvolveController_0.startWS)
      }
  
    // @LINE:37
    case controllers_Assets_versioned15_route(params@_) =>
      call(params.fromPath[String]("file", None)) { (file) =>
        controllers_Assets_versioned15_invoker.call(Assets_5.versioned(file))
      }
  
    // @LINE:41
    case controllers_UserController_showLoginForm16_route(params@_) =>
      call { 
        controllers_UserController_showLoginForm16_invoker.call(UserController_1.showLoginForm)
      }
  
    // @LINE:42
    case controllers_UserController_processLoginAttempt17_route(params@_) =>
      call { 
        controllers_UserController_processLoginAttempt17_invoker.call(UserController_1.processLoginAttempt)
      }
  
    // @LINE:45
    case controllers_AuthenticatedUserController_logout18_route(params@_) =>
      call { 
        controllers_AuthenticatedUserController_logout18_invoker.call(AuthenticatedUserController_2.logout)
      }
  
    // @LINE:46
    case controllers_LandingPageController_showLandingPage19_route(params@_) =>
      call { 
        controllers_LandingPageController_showLandingPage19_invoker.call(LandingPageController_7.showLandingPage)
      }
  
    // @LINE:49
    case prefixed_webjars_Routes_0_20(handler) => handler
  }
}
