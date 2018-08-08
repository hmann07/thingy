// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/train-engine/conf/routes
// @DATE:Mon Jul 23 23:01:17 BST 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:6
package controllers {

  // @LINE:31
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:31
    def versioned(file:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[String]].unbind("file", file))
    }
  
  }

  // @LINE:39
  class ReverseAuthenticatedUserController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:39
    def logout(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "users/logout")
    }
  
  }

  // @LINE:10
  class ReverseMongoLookupController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:22
    def getGenerationsByRun(runId:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "generations/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("runId", runId)))
    }
  
    // @LINE:24
    def getRuns(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "runs")
    }
  
    // @LINE:12
    def getSpeciesByGeneration(runId:String, generationId:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "getSpeciesByGeneration/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("runId", runId)) + "/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("generationId", generationId)))
    }
  
    // @LINE:20
    def getGenerations(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "generations")
    }
  
    // @LINE:18
    def findBySpeciesandGen(runId:String, generationId:String, speciesId:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "getNetByGenerationAndSpecies/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("runId", runId)) + "/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("generationId", generationId)) + "/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("speciesId", speciesId)))
    }
  
    // @LINE:10
    def findBySpecies(speciesId:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "mongolookup/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("speciesId", speciesId)))
    }
  
  }

  // @LINE:14
  class ReverseConfigController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:16
    def userPost(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "userPost")
    }
  
    // @LINE:14
    def viewConfig(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "config")
    }
  
  }

  // @LINE:8
  class ReverseNetViewerController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:8
    def index(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "netviewer")
    }
  
  }

  // @LINE:26
  class ReverseStartEvolveController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:28
    def startWS(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "startWS")
    }
  
    // @LINE:26
    def start(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "start")
    }
  
  }

  // @LINE:35
  class ReverseUserController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:36
    def processLoginAttempt(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "users/doLogin")
    }
  
    // @LINE:35
    def showLoginForm(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "users/login")
    }
  
  }

  // @LINE:6
  class ReverseLandingPageController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:6
    def showLandingPage(): Call = {
    
      () match {
      
        // @LINE:6
        case ()  =>
          
          Call("GET", _prefix)
      
      }
    
    }
  
  }


}
