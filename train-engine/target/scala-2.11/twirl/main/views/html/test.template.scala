
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

object test extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[Call,AssetsFinder,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(logoutUrl: Call)(implicit assetsFinder: AssetsFinder):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.56*/("""

"""),format.raw/*3.1*/("""<!DOCTYPE html>
<html>
<head>
	<title></title>
	<link rel="stylesheet" media="screen" href=""""),_display_(/*7.47*/assetsFinder/*7.59*/.path("stylesheets/app.css")),format.raw/*7.87*/("""">
  <link rel="stylesheet" media="screen" href=""""),_display_(/*8.48*/routes/*8.54*/.Assets.versioned("lib/bootstrap/css/bootstrap.min.css")),format.raw/*8.110*/("""">
  <script src=""""),_display_(/*9.17*/assetsFinder/*9.29*/.path("javascripts/jquery.js")),format.raw/*9.59*/("""" type="text/javascript"></script>

  
  <script type="text/javascript" src=""""),_display_(/*12.40*/routes/*12.46*/.Assets.versioned("lib/react-dom/umd/react-dom.development.js")),format.raw/*12.109*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*13.40*/routes/*13.46*/.Assets.versioned("lib/crossfilter2/crossfilter.min.js")),format.raw/*13.102*/(""""></script>
</head>
<body>
"""),_display_(/*16.2*/navbar(logoutUrl)),format.raw/*16.19*/("""
"""),format.raw/*17.1*/("""<div class="container">
<h1>Test Output</h1>
 <div class="progress">
  <div class="progress-bar progress-bar-striped" role="progressbar" style="width: 10%" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100"></div>
</div>
  <div id="output">
  </div>
</div>
<script src=""""),_display_(/*25.15*/assetsFinder/*25.27*/.path("javascripts/testprogress.js")),format.raw/*25.63*/("""" type="text/javascript"></script>
<script src=""""),_display_(/*26.15*/assetsFinder/*26.27*/.path("javascripts/d3-v4.js")),format.raw/*26.56*/("""" type="text/javascript"></script>

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
                  DATE: Sun Aug 19 18:00:05 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/train-engine/app/views/test.scala.html
                  HASH: e2729bcc6a4982b00fd4a20512a1335bd8219d5d
                  MATRIX: 739->1|888->55|918->59|1041->156|1061->168|1109->196|1186->247|1200->253|1277->309|1323->329|1343->341|1393->371|1501->452|1516->458|1601->521|1680->573|1695->579|1773->635|1830->666|1868->683|1897->685|2209->970|2230->982|2287->1018|2364->1068|2385->1080|2435->1109
                  LINES: 21->1|26->1|28->3|32->7|32->7|32->7|33->8|33->8|33->8|34->9|34->9|34->9|37->12|37->12|37->12|38->13|38->13|38->13|41->16|41->16|42->17|50->25|50->25|50->25|51->26|51->26|51->26
                  -- GENERATED --
              */
          