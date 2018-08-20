// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/train-engine/conf/routes
// @DATE:Mon Aug 20 21:25:37 BST 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset

// @LINE:6
package controllers.javascript {

  // @LINE:43
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:43
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("file", file0)})
        }
      """
    )
  
  }

  // @LINE:28
  class ReverseNewEnvironmentController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:28
    def newEnvironment: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.NewEnvironmentController.newEnvironment",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "newenvironment"})
        }
      """
    )
  
  }

  // @LINE:51
  class ReverseAuthenticatedUserController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:51
    def logout: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.AuthenticatedUserController.logout",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "users/logout"})
        }
      """
    )
  
  }

  // @LINE:10
  class ReverseMongoLookupController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:34
    def getGenerationsByRun: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.MongoLookupController.getGenerationsByRun",
      """
        function(runId0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "generations/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("runId", runId0))})
        }
      """
    )
  
    // @LINE:36
    def getRuns: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.MongoLookupController.getRuns",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "runs"})
        }
      """
    )
  
    // @LINE:12
    def getSpeciesByGeneration: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.MongoLookupController.getSpeciesByGeneration",
      """
        function(runId0,generationId1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "getSpeciesByGeneration/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("runId", runId0)) + "/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("generationId", generationId1))})
        }
      """
    )
  
    // @LINE:32
    def getGenerations: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.MongoLookupController.getGenerations",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "generations"})
        }
      """
    )
  
    // @LINE:30
    def findBySpeciesandGen: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.MongoLookupController.findBySpeciesandGen",
      """
        function(runId0,generationId1,speciesId2) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "getNetByGenerationAndSpecies/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("runId", runId0)) + "/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("generationId", generationId1)) + "/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("speciesId", speciesId2))})
        }
      """
    )
  
    // @LINE:10
    def findBySpecies: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.MongoLookupController.findBySpecies",
      """
        function(speciesId0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "mongolookup/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("speciesId", speciesId0))})
        }
      """
    )
  
  }

  // @LINE:14
  class ReverseConfigController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:18
    def viewConfigFilled: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ConfigController.viewConfigFilled",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "configFilled"})
        }
      """
    )
  
    // @LINE:22
    def userPost: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ConfigController.userPost",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "userPost"})
        }
      """
    )
  
    // @LINE:20
    def reuseConfig: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ConfigController.reuseConfig",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "config/reuseConfig"})
        }
      """
    )
  
    // @LINE:14
    def viewConfig: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ConfigController.viewConfig",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "config"})
        }
      """
    )
  
  }

  // @LINE:16
  class ReverseAggregateAnalysisController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:16
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.AggregateAnalysisController.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "aggregateAnalysis"})
        }
      """
    )
  
  }

  // @LINE:8
  class ReverseNetViewerController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:8
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.NetViewerController.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "netviewer"})
        }
      """
    )
  
  }

  // @LINE:38
  class ReverseStartEvolveController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:40
    def startWS: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.StartEvolveController.startWS",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "startWS"})
        }
      """
    )
  
    // @LINE:38
    def start: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.StartEvolveController.start",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "start"})
        }
      """
    )
  
  }

  // @LINE:47
  class ReverseUserController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:48
    def processLoginAttempt: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserController.processLoginAttempt",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "users/doLogin"})
        }
      """
    )
  
    // @LINE:47
    def showLoginForm: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserController.showLoginForm",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "users/login"})
        }
      """
    )
  
  }

  // @LINE:24
  class ReverseStartTestController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:26
    def startWS: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.StartTestController.startWS",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "test/startWS"})
        }
      """
    )
  
    // @LINE:24
    def submitGenome: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.StartTestController.submitGenome",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "submitgenome"})
        }
      """
    )
  
  }

  // @LINE:6
  class ReverseLandingPageController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:6
    def showLandingPage: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.LandingPageController.showLandingPage",
      """
        function() {
        
          if (true) {
            return _wA({method:"GET", url:"""" + _prefix + """"})
          }
        
        }
      """
    )
  
  }


}
