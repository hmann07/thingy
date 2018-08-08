// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/train-engine/conf/routes
// @DATE:Mon Jul 23 23:01:17 BST 2018

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseAuthenticatedUserController AuthenticatedUserController = new controllers.ReverseAuthenticatedUserController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseMongoLookupController MongoLookupController = new controllers.ReverseMongoLookupController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseConfigController ConfigController = new controllers.ReverseConfigController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseNetViewerController NetViewerController = new controllers.ReverseNetViewerController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseStartEvolveController StartEvolveController = new controllers.ReverseStartEvolveController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseUserController UserController = new controllers.ReverseUserController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseLandingPageController LandingPageController = new controllers.ReverseLandingPageController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseAuthenticatedUserController AuthenticatedUserController = new controllers.javascript.ReverseAuthenticatedUserController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseMongoLookupController MongoLookupController = new controllers.javascript.ReverseMongoLookupController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseConfigController ConfigController = new controllers.javascript.ReverseConfigController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseNetViewerController NetViewerController = new controllers.javascript.ReverseNetViewerController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseStartEvolveController StartEvolveController = new controllers.javascript.ReverseStartEvolveController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseUserController UserController = new controllers.javascript.ReverseUserController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseLandingPageController LandingPageController = new controllers.javascript.ReverseLandingPageController(RoutesPrefix.byNamePrefix());
  }

}
