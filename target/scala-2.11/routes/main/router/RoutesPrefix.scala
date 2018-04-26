// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/mau/Documents/thingy/conf/routes
// @DATE:Thu Apr 26 22:15:04 BST 2018


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
