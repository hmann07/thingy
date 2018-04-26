// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/conf/routes
// @DATE:Thu Apr 26 22:15:04 BST 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:6
package controllers {

  // @LINE:26
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:26
    def versioned(file:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[String]].unbind("file", file))
    }
  
  }

  // @LINE:11
  class ReverseMongoLookupController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:16
    def getGenerations(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "generations")
    }
  
    // @LINE:11
    def findBySpecies(speciesId:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "mongolookup/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("speciesId", speciesId)))
    }
  
    // @LINE:13
    def getSpeciesByGeneration(generationId:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "getSpeciesByGeneration/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("generationId", generationId)))
    }
  
  }

  // @LINE:21
  class ReverseCountController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:21
    def count(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "count")
    }
  
  }

  // @LINE:9
  class ReverseNetViewerController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def index(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "netviewer")
    }
  
  }

  // @LINE:18
  class ReverseStartEvolveController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:18
    def start(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "start")
    }
  
  }

  // @LINE:6
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:6
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
  }

  // @LINE:23
  class ReverseAsyncController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:23
    def message(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "message")
    }
  
  }


}
