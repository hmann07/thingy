// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/train-engine/conf/routes
// @DATE:Mon Aug 20 21:25:37 BST 2018

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  LandingPageController_9: controllers.LandingPageController,
  // @LINE:8
  NetViewerController_10: controllers.NetViewerController,
  // @LINE:10
  MongoLookupController_8: controllers.MongoLookupController,
  // @LINE:14
  ConfigController_6: controllers.ConfigController,
  // @LINE:16
  AggregateAnalysisController_4: controllers.AggregateAnalysisController,
  // @LINE:24
  StartTestController_3: controllers.StartTestController,
  // @LINE:28
  NewEnvironmentController_5: controllers.NewEnvironmentController,
  // @LINE:38
  StartEvolveController_0: controllers.StartEvolveController,
  // @LINE:43
  Assets_7: controllers.Assets,
  // @LINE:47
  UserController_1: controllers.UserController,
  // @LINE:51
  AuthenticatedUserController_2: controllers.AuthenticatedUserController,
  // @LINE:55
  webjars_Routes_0: webjars.Routes,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    LandingPageController_9: controllers.LandingPageController,
    // @LINE:8
    NetViewerController_10: controllers.NetViewerController,
    // @LINE:10
    MongoLookupController_8: controllers.MongoLookupController,
    // @LINE:14
    ConfigController_6: controllers.ConfigController,
    // @LINE:16
    AggregateAnalysisController_4: controllers.AggregateAnalysisController,
    // @LINE:24
    StartTestController_3: controllers.StartTestController,
    // @LINE:28
    NewEnvironmentController_5: controllers.NewEnvironmentController,
    // @LINE:38
    StartEvolveController_0: controllers.StartEvolveController,
    // @LINE:43
    Assets_7: controllers.Assets,
    // @LINE:47
    UserController_1: controllers.UserController,
    // @LINE:51
    AuthenticatedUserController_2: controllers.AuthenticatedUserController,
    // @LINE:55
    webjars_Routes_0: webjars.Routes
  ) = this(errorHandler, LandingPageController_9, NetViewerController_10, MongoLookupController_8, ConfigController_6, AggregateAnalysisController_4, StartTestController_3, NewEnvironmentController_5, StartEvolveController_0, Assets_7, UserController_1, AuthenticatedUserController_2, webjars_Routes_0, "/")

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, LandingPageController_9, NetViewerController_10, MongoLookupController_8, ConfigController_6, AggregateAnalysisController_4, StartTestController_3, NewEnvironmentController_5, StartEvolveController_0, Assets_7, UserController_1, AuthenticatedUserController_2, webjars_Routes_0, prefix)
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
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """submitgenome""", """controllers.StartTestController.submitGenome"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """test/startWS""", """controllers.StartTestController.startWS"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """newenvironment""", """controllers.NewEnvironmentController.newEnvironment"""),
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
    prefixed_webjars_Routes_0_23.router.documentation,
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
    LandingPageController_9.showLandingPage,
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
    NetViewerController_10.index,
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
    MongoLookupController_8.findBySpecies(fakeValue[String]),
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
    MongoLookupController_8.getSpeciesByGeneration(fakeValue[String], fakeValue[String]),
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
    ConfigController_6.viewConfig,
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
    AggregateAnalysisController_4.index,
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
    ConfigController_6.viewConfigFilled,
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
    ConfigController_6.reuseConfig,
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
    ConfigController_6.userPost,
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
  private[this] lazy val controllers_StartTestController_submitGenome9_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("submitgenome")))
  )
  private[this] lazy val controllers_StartTestController_submitGenome9_invoker = createInvoker(
    StartTestController_3.submitGenome,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.StartTestController",
      "submitGenome",
      Nil,
      "POST",
      this.prefix + """submitgenome""",
      """""",
      Seq()
    )
  )

  // @LINE:26
  private[this] lazy val controllers_StartTestController_startWS10_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("test/startWS")))
  )
  private[this] lazy val controllers_StartTestController_startWS10_invoker = createInvoker(
    StartTestController_3.startWS,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.StartTestController",
      "startWS",
      Nil,
      "GET",
      this.prefix + """test/startWS""",
      """""",
      Seq()
    )
  )

  // @LINE:28
  private[this] lazy val controllers_NewEnvironmentController_newEnvironment11_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("newenvironment")))
  )
  private[this] lazy val controllers_NewEnvironmentController_newEnvironment11_invoker = createInvoker(
    NewEnvironmentController_5.newEnvironment,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.NewEnvironmentController",
      "newEnvironment",
      Nil,
      "GET",
      this.prefix + """newenvironment""",
      """""",
      Seq()
    )
  )

  // @LINE:30
  private[this] lazy val controllers_MongoLookupController_findBySpeciesandGen12_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("getNetByGenerationAndSpecies/"), DynamicPart("runId", """[^/]+""",true), StaticPart("/"), DynamicPart("generationId", """[^/]+""",true), StaticPart("/"), DynamicPart("speciesId", """[^/]+""",true)))
  )
  private[this] lazy val controllers_MongoLookupController_findBySpeciesandGen12_invoker = createInvoker(
    MongoLookupController_8.findBySpeciesandGen(fakeValue[String], fakeValue[String], fakeValue[String]),
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

  // @LINE:32
  private[this] lazy val controllers_MongoLookupController_getGenerations13_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("generations")))
  )
  private[this] lazy val controllers_MongoLookupController_getGenerations13_invoker = createInvoker(
    MongoLookupController_8.getGenerations,
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

  // @LINE:34
  private[this] lazy val controllers_MongoLookupController_getGenerationsByRun14_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("generations/"), DynamicPart("runId", """[^/]+""",true)))
  )
  private[this] lazy val controllers_MongoLookupController_getGenerationsByRun14_invoker = createInvoker(
    MongoLookupController_8.getGenerationsByRun(fakeValue[String]),
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

  // @LINE:36
  private[this] lazy val controllers_MongoLookupController_getRuns15_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("runs")))
  )
  private[this] lazy val controllers_MongoLookupController_getRuns15_invoker = createInvoker(
    MongoLookupController_8.getRuns,
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

  // @LINE:38
  private[this] lazy val controllers_StartEvolveController_start16_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("start")))
  )
  private[this] lazy val controllers_StartEvolveController_start16_invoker = createInvoker(
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

  // @LINE:40
  private[this] lazy val controllers_StartEvolveController_startWS17_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("startWS")))
  )
  private[this] lazy val controllers_StartEvolveController_startWS17_invoker = createInvoker(
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

  // @LINE:43
  private[this] lazy val controllers_Assets_versioned18_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned18_invoker = createInvoker(
    Assets_7.versioned(fakeValue[String]),
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

  // @LINE:47
  private[this] lazy val controllers_UserController_showLoginForm19_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/login")))
  )
  private[this] lazy val controllers_UserController_showLoginForm19_invoker = createInvoker(
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

  // @LINE:48
  private[this] lazy val controllers_UserController_processLoginAttempt20_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/doLogin")))
  )
  private[this] lazy val controllers_UserController_processLoginAttempt20_invoker = createInvoker(
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

  // @LINE:51
  private[this] lazy val controllers_AuthenticatedUserController_logout21_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("users/logout")))
  )
  private[this] lazy val controllers_AuthenticatedUserController_logout21_invoker = createInvoker(
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

  // @LINE:52
  private[this] lazy val controllers_LandingPageController_showLandingPage22_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("landingPage")))
  )
  private[this] lazy val controllers_LandingPageController_showLandingPage22_invoker = createInvoker(
    LandingPageController_9.showLandingPage,
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

  // @LINE:55
  private[this] val prefixed_webjars_Routes_0_23 = Include(webjars_Routes_0.withPrefix(this.prefix + (if (this.prefix.endsWith("/")) "" else "/") + "webjars"))


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_LandingPageController_showLandingPage0_route(params@_) =>
      call { 
        controllers_LandingPageController_showLandingPage0_invoker.call(LandingPageController_9.showLandingPage)
      }
  
    // @LINE:8
    case controllers_NetViewerController_index1_route(params@_) =>
      call { 
        controllers_NetViewerController_index1_invoker.call(NetViewerController_10.index)
      }
  
    // @LINE:10
    case controllers_MongoLookupController_findBySpecies2_route(params@_) =>
      call(params.fromPath[String]("speciesId", None)) { (speciesId) =>
        controllers_MongoLookupController_findBySpecies2_invoker.call(MongoLookupController_8.findBySpecies(speciesId))
      }
  
    // @LINE:12
    case controllers_MongoLookupController_getSpeciesByGeneration3_route(params@_) =>
      call(params.fromPath[String]("runId", None), params.fromPath[String]("generationId", None)) { (runId, generationId) =>
        controllers_MongoLookupController_getSpeciesByGeneration3_invoker.call(MongoLookupController_8.getSpeciesByGeneration(runId, generationId))
      }
  
    // @LINE:14
    case controllers_ConfigController_viewConfig4_route(params@_) =>
      call { 
        controllers_ConfigController_viewConfig4_invoker.call(ConfigController_6.viewConfig)
      }
  
    // @LINE:16
    case controllers_AggregateAnalysisController_index5_route(params@_) =>
      call { 
        controllers_AggregateAnalysisController_index5_invoker.call(AggregateAnalysisController_4.index)
      }
  
    // @LINE:18
    case controllers_ConfigController_viewConfigFilled6_route(params@_) =>
      call { 
        controllers_ConfigController_viewConfigFilled6_invoker.call(ConfigController_6.viewConfigFilled)
      }
  
    // @LINE:20
    case controllers_ConfigController_reuseConfig7_route(params@_) =>
      call { 
        controllers_ConfigController_reuseConfig7_invoker.call(ConfigController_6.reuseConfig)
      }
  
    // @LINE:22
    case controllers_ConfigController_userPost8_route(params@_) =>
      call { 
        controllers_ConfigController_userPost8_invoker.call(ConfigController_6.userPost)
      }
  
    // @LINE:24
    case controllers_StartTestController_submitGenome9_route(params@_) =>
      call { 
        controllers_StartTestController_submitGenome9_invoker.call(StartTestController_3.submitGenome)
      }
  
    // @LINE:26
    case controllers_StartTestController_startWS10_route(params@_) =>
      call { 
        controllers_StartTestController_startWS10_invoker.call(StartTestController_3.startWS)
      }
  
    // @LINE:28
    case controllers_NewEnvironmentController_newEnvironment11_route(params@_) =>
      call { 
        controllers_NewEnvironmentController_newEnvironment11_invoker.call(NewEnvironmentController_5.newEnvironment)
      }
  
    // @LINE:30
    case controllers_MongoLookupController_findBySpeciesandGen12_route(params@_) =>
      call(params.fromPath[String]("runId", None), params.fromPath[String]("generationId", None), params.fromPath[String]("speciesId", None)) { (runId, generationId, speciesId) =>
        controllers_MongoLookupController_findBySpeciesandGen12_invoker.call(MongoLookupController_8.findBySpeciesandGen(runId, generationId, speciesId))
      }
  
    // @LINE:32
    case controllers_MongoLookupController_getGenerations13_route(params@_) =>
      call { 
        controllers_MongoLookupController_getGenerations13_invoker.call(MongoLookupController_8.getGenerations)
      }
  
    // @LINE:34
    case controllers_MongoLookupController_getGenerationsByRun14_route(params@_) =>
      call(params.fromPath[String]("runId", None)) { (runId) =>
        controllers_MongoLookupController_getGenerationsByRun14_invoker.call(MongoLookupController_8.getGenerationsByRun(runId))
      }
  
    // @LINE:36
    case controllers_MongoLookupController_getRuns15_route(params@_) =>
      call { 
        controllers_MongoLookupController_getRuns15_invoker.call(MongoLookupController_8.getRuns)
      }
  
    // @LINE:38
    case controllers_StartEvolveController_start16_route(params@_) =>
      call { 
        controllers_StartEvolveController_start16_invoker.call(StartEvolveController_0.start)
      }
  
    // @LINE:40
    case controllers_StartEvolveController_startWS17_route(params@_) =>
      call { 
        controllers_StartEvolveController_startWS17_invoker.call(StartEvolveController_0.startWS)
      }
  
    // @LINE:43
    case controllers_Assets_versioned18_route(params@_) =>
      call(params.fromPath[String]("file", None)) { (file) =>
        controllers_Assets_versioned18_invoker.call(Assets_7.versioned(file))
      }
  
    // @LINE:47
    case controllers_UserController_showLoginForm19_route(params@_) =>
      call { 
        controllers_UserController_showLoginForm19_invoker.call(UserController_1.showLoginForm)
      }
  
    // @LINE:48
    case controllers_UserController_processLoginAttempt20_route(params@_) =>
      call { 
        controllers_UserController_processLoginAttempt20_invoker.call(UserController_1.processLoginAttempt)
      }
  
    // @LINE:51
    case controllers_AuthenticatedUserController_logout21_route(params@_) =>
      call { 
        controllers_AuthenticatedUserController_logout21_invoker.call(AuthenticatedUserController_2.logout)
      }
  
    // @LINE:52
    case controllers_LandingPageController_showLandingPage22_route(params@_) =>
      call { 
        controllers_LandingPageController_showLandingPage22_invoker.call(LandingPageController_9.showLandingPage)
      }
  
    // @LINE:55
    case prefixed_webjars_Routes_0_23(handler) => handler
  }
}
