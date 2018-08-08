// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/train-engine/conf/routes
// @DATE:Mon Jul 23 23:01:17 BST 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset

// @LINE:6
package controllers.javascript {

  // @LINE:31
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:31
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("file", file0)})
        }
      """
    )
  
  }

  // @LINE:39
  class ReverseAuthenticatedUserController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:39
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

  
    // @LINE:22
    def getGenerationsByRun: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.MongoLookupController.getGenerationsByRun",
      """
        function(runId0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "generations/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("runId", runId0))})
        }
      """
    )
  
    // @LINE:24
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
  
    // @LINE:20
    def getGenerations: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.MongoLookupController.getGenerations",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "generations"})
        }
      """
    )
  
    // @LINE:18
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

  
    // @LINE:16
    def userPost: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ConfigController.userPost",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "userPost"})
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

  // @LINE:26
  class ReverseStartEvolveController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:28
    def startWS: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.StartEvolveController.startWS",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "startWS"})
        }
      """
    )
  
    // @LINE:26
    def start: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.StartEvolveController.start",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "start"})
        }
      """
    )
  
  }

  // @LINE:35
  class ReverseUserController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:36
    def processLoginAttempt: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserController.processLoginAttempt",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "users/doLogin"})
        }
      """
    )
  
    // @LINE:35
    def showLoginForm: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.UserController.showLoginForm",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "users/login"})
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