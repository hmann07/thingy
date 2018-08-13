// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/train-engine/conf/routes
// @DATE:Sun Aug 12 19:41:21 BST 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:6
package controllers {

  // @LINE:37
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:37
    def versioned(file:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[String]].unbind("file", file))
    }
  
  }

  // @LINE:45
  class ReverseAuthenticatedUserController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:45
    def logout(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "users/logout")
    }
  
  }

  // @LINE:10
  class ReverseMongoLookupController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:28
    def getGenerationsByRun(runId:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "generations/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("runId", runId)))
    }
  
    // @LINE:30
    def getRuns(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "runs")
    }
  
    // @LINE:12
    def getSpeciesByGeneration(runId:String, generationId:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "getSpeciesByGeneration/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("runId", runId)) + "/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("generationId", generationId)))
    }
  
    // @LINE:26
    def getGenerations(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "generations")
    }
  
    // @LINE:24
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

  
    // @LINE:18
    def viewConfigFilled(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "configFilled")
    }
  
    // @LINE:22
    def userPost(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "userPost")
    }
  
    // @LINE:20
    def reuseConfig(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "config/reuseConfig")
    }
  
    // @LINE:14
    def viewConfig(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "config")
    }
  
  }

  // @LINE:16
  class ReverseAggregateAnalysisController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:16
    def index(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "aggregateAnalysis")
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

  // @LINE:32
  class ReverseStartEvolveController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:34
    def startWS(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "startWS")
    }
  
    // @LINE:32
    def start(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "start")
    }
  
  }

  // @LINE:41
  class ReverseUserController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:42
    def processLoginAttempt(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "users/doLogin")
    }
  
    // @LINE:41
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
