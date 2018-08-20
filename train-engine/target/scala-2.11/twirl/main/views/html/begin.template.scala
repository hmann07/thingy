
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

object begin extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[Call,AssetsFinder,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(logoutUrl: Call)(implicit assetsFinder: AssetsFinder):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.56*/("""


"""),format.raw/*4.1*/("""<!DOCTYPE html>
<html>
<head>
	<title></title>
	<link rel="stylesheet" media="screen" href=""""),_display_(/*8.47*/assetsFinder/*8.59*/.path("stylesheets/app.css")),format.raw/*8.87*/("""">
  <link rel="stylesheet" media="screen" href=""""),_display_(/*9.48*/routes/*9.54*/.Assets.versioned("lib/bootstrap/css/bootstrap.min.css")),format.raw/*9.110*/("""">
  <script src=""""),_display_(/*10.17*/assetsFinder/*10.29*/.path("javascripts/jquery.js")),format.raw/*10.59*/("""" type="text/javascript"></script>

  
  <script type="text/javascript" src=""""),_display_(/*13.40*/routes/*13.46*/.Assets.versioned("lib/react-dom/umd/react-dom.development.js")),format.raw/*13.109*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*14.40*/routes/*14.46*/.Assets.versioned("lib/crossfilter2/crossfilter.min.js")),format.raw/*14.102*/(""""></script>
</head>
<body>

<div class="container">
	<div class="jumbotron">
	  <h1 class="display-4">Evolve!</h1>
	  <p class="lead">create an adaptive system!</p>
	  <hr class="my-4">
	  <p>click here to set up a new system</p>
	  <p class="lead">
	    <a class="btn btn-primary btn-lg" href="/newenvironment" role="button">New Experiment</a>
	  </p>
	</div>
</div>
<script src=""""),_display_(/*29.15*/assetsFinder/*29.27*/.path("javascripts/websocketprogress.js")),format.raw/*29.68*/("""" type="text/javascript"></script>
<script src=""""),_display_(/*30.15*/assetsFinder/*30.27*/.path("javascripts/d3-v4.js")),format.raw/*30.56*/("""" type="text/javascript"></script>
</body>
</html>"""))
      }
    }
  }

  def render(logoutUrl:Call,assetsFinder:AssetsFinder): play.twirl.api.HtmlFormat.Appendable = apply(logoutUrl)(assetsFinder)

  def f:((Call) => (AssetsFinder) => play.twirl.api.HtmlFormat.Appendable) = (logoutUrl) => (assetsFinder) => apply(logoutUrl)(assetsFinder)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Mon Aug 20 21:48:25 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/train-engine/app/views/begin.scala.html
                  HASH: ad41e192e284b698d99e2be7be87a11938f72013
                  MATRIX: 740->1|889->55|921->61|1044->158|1064->170|1112->198|1189->249|1203->255|1280->311|1327->331|1348->343|1399->373|1507->454|1522->460|1607->523|1686->575|1701->581|1779->637|2203->1034|2224->1046|2286->1087|2363->1137|2384->1149|2434->1178
                  LINES: 21->1|26->1|29->4|33->8|33->8|33->8|34->9|34->9|34->9|35->10|35->10|35->10|38->13|38->13|38->13|39->14|39->14|39->14|54->29|54->29|54->29|55->30|55->30|55->30
                  -- GENERATED --
              */
          