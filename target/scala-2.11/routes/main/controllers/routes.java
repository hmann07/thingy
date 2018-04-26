// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/conf/routes
// @DATE:Thu Apr 26 22:15:04 BST 2018

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseMongoLookupController MongoLookupController = new controllers.ReverseMongoLookupController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseCountController CountController = new controllers.ReverseCountController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseNetViewerController NetViewerController = new controllers.ReverseNetViewerController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseStartEvolveController StartEvolveController = new controllers.ReverseStartEvolveController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseHomeController HomeController = new controllers.ReverseHomeController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseAsyncController AsyncController = new controllers.ReverseAsyncController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseMongoLookupController MongoLookupController = new controllers.javascript.ReverseMongoLookupController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseCountController CountController = new controllers.javascript.ReverseCountController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseNetViewerController NetViewerController = new controllers.javascript.ReverseNetViewerController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseStartEvolveController StartEvolveController = new controllers.javascript.ReverseStartEvolveController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseHomeController HomeController = new controllers.javascript.ReverseHomeController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseAsyncController AsyncController = new controllers.javascript.ReverseAsyncController(RoutesPrefix.byNamePrefix());
  }

}
