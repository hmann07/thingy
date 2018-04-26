// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/conf/routes
// @DATE:Thu Apr 26 22:15:04 BST 2018

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  HomeController_1: controllers.HomeController,
  // @LINE:9
  NetViewerController_6: controllers.NetViewerController,
  // @LINE:11
  MongoLookupController_5: controllers.MongoLookupController,
  // @LINE:18
  StartEvolveController_3: controllers.StartEvolveController,
  // @LINE:21
  CountController_0: controllers.CountController,
  // @LINE:23
  AsyncController_2: controllers.AsyncController,
  // @LINE:26
  Assets_4: controllers.Assets,
  // @LINE:28
  webjars_Routes_0: webjars.Routes,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    HomeController_1: controllers.HomeController,
    // @LINE:9
    NetViewerController_6: controllers.NetViewerController,
    // @LINE:11
    MongoLookupController_5: controllers.MongoLookupController,
    // @LINE:18
    StartEvolveController_3: controllers.StartEvolveController,
    // @LINE:21
    CountController_0: controllers.CountController,
    // @LINE:23
    AsyncController_2: controllers.AsyncController,
    // @LINE:26
    Assets_4: controllers.Assets,
    // @LINE:28
    webjars_Routes_0: webjars.Routes
  ) = this(errorHandler, HomeController_1, NetViewerController_6, MongoLookupController_5, StartEvolveController_3, CountController_0, AsyncController_2, Assets_4, webjars_Routes_0, "/")

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HomeController_1, NetViewerController_6, MongoLookupController_5, StartEvolveController_3, CountController_0, AsyncController_2, Assets_4, webjars_Routes_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.HomeController.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """netviewer""", """controllers.NetViewerController.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """mongolookup/""" + "$" + """speciesId<[^/]+>""", """controllers.MongoLookupController.findBySpecies(speciesId:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """getSpeciesByGeneration/""" + "$" + """generationId<[^/]+>""", """controllers.MongoLookupController.getSpeciesByGeneration(generationId:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """generations""", """controllers.MongoLookupController.getGenerations"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """start""", """controllers.StartEvolveController.start"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """count""", """controllers.CountController.count"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """message""", """controllers.AsyncController.message"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(file:String)"""),
    prefixed_webjars_Routes_0_9.router.documentation,
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:6
  private[this] lazy val controllers_HomeController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_HomeController_index0_invoker = createInvoker(
    HomeController_1.index,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "index",
      Nil,
      "GET",
      this.prefix + """""",
      """ An example controller showing a sample home page""",
      Seq()
    )
  )

  // @LINE:9
  private[this] lazy val controllers_NetViewerController_index1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("netviewer")))
  )
  private[this] lazy val controllers_NetViewerController_index1_invoker = createInvoker(
    NetViewerController_6.index,
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

  // @LINE:11
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

  // @LINE:13
  private[this] lazy val controllers_MongoLookupController_getSpeciesByGeneration3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("getSpeciesByGeneration/"), DynamicPart("generationId", """[^/]+""",true)))
  )
  private[this] lazy val controllers_MongoLookupController_getSpeciesByGeneration3_invoker = createInvoker(
    MongoLookupController_5.getSpeciesByGeneration(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.MongoLookupController",
      "getSpeciesByGeneration",
      Seq(classOf[String]),
      "GET",
      this.prefix + """getSpeciesByGeneration/""" + "$" + """generationId<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:16
  private[this] lazy val controllers_MongoLookupController_getGenerations4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("generations")))
  )
  private[this] lazy val controllers_MongoLookupController_getGenerations4_invoker = createInvoker(
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

  // @LINE:18
  private[this] lazy val controllers_StartEvolveController_start5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("start")))
  )
  private[this] lazy val controllers_StartEvolveController_start5_invoker = createInvoker(
    StartEvolveController_3.start,
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

  // @LINE:21
  private[this] lazy val controllers_CountController_count6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("count")))
  )
  private[this] lazy val controllers_CountController_count6_invoker = createInvoker(
    CountController_0.count,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.CountController",
      "count",
      Nil,
      "GET",
      this.prefix + """count""",
      """ An example controller showing how to use dependency injection""",
      Seq()
    )
  )

  // @LINE:23
  private[this] lazy val controllers_AsyncController_message7_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("message")))
  )
  private[this] lazy val controllers_AsyncController_message7_invoker = createInvoker(
    AsyncController_2.message,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.AsyncController",
      "message",
      Nil,
      "GET",
      this.prefix + """message""",
      """ An example controller showing how to write asynchronous code""",
      Seq()
    )
  )

  // @LINE:26
  private[this] lazy val controllers_Assets_versioned8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned8_invoker = createInvoker(
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

  // @LINE:28
  private[this] val prefixed_webjars_Routes_0_9 = Include(webjars_Routes_0.withPrefix(this.prefix + (if (this.prefix.endsWith("/")) "" else "/") + "webjars"))


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_HomeController_index0_route(params@_) =>
      call { 
        controllers_HomeController_index0_invoker.call(HomeController_1.index)
      }
  
    // @LINE:9
    case controllers_NetViewerController_index1_route(params@_) =>
      call { 
        controllers_NetViewerController_index1_invoker.call(NetViewerController_6.index)
      }
  
    // @LINE:11
    case controllers_MongoLookupController_findBySpecies2_route(params@_) =>
      call(params.fromPath[String]("speciesId", None)) { (speciesId) =>
        controllers_MongoLookupController_findBySpecies2_invoker.call(MongoLookupController_5.findBySpecies(speciesId))
      }
  
    // @LINE:13
    case controllers_MongoLookupController_getSpeciesByGeneration3_route(params@_) =>
      call(params.fromPath[String]("generationId", None)) { (generationId) =>
        controllers_MongoLookupController_getSpeciesByGeneration3_invoker.call(MongoLookupController_5.getSpeciesByGeneration(generationId))
      }
  
    // @LINE:16
    case controllers_MongoLookupController_getGenerations4_route(params@_) =>
      call { 
        controllers_MongoLookupController_getGenerations4_invoker.call(MongoLookupController_5.getGenerations)
      }
  
    // @LINE:18
    case controllers_StartEvolveController_start5_route(params@_) =>
      call { 
        controllers_StartEvolveController_start5_invoker.call(StartEvolveController_3.start)
      }
  
    // @LINE:21
    case controllers_CountController_count6_route(params@_) =>
      call { 
        controllers_CountController_count6_invoker.call(CountController_0.count)
      }
  
    // @LINE:23
    case controllers_AsyncController_message7_route(params@_) =>
      call { 
        controllers_AsyncController_message7_invoker.call(AsyncController_2.message)
      }
  
    // @LINE:26
    case controllers_Assets_versioned8_route(params@_) =>
      call(params.fromPath[String]("file", None)) { (file) =>
        controllers_Assets_versioned8_invoker.call(Assets_4.versioned(file))
      }
  
    // @LINE:28
    case prefixed_webjars_Routes_0_9(handler) => handler
  }
}
