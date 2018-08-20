
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

object aggregateAnalysis extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template4[String,Call,AssetsFinder,RequestHeader,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(message: String, logoutUrl: Call)(implicit assetsFinder: AssetsFinder, request: RequestHeader):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.97*/("""

"""),format.raw/*3.1*/("""<!DOCTYPE html>
<html>
<head>
	<title></title>
	<link rel="stylesheet" media="screen" href=""""),_display_(/*7.47*/assetsFinder/*7.59*/.path("stylesheets/app.css")),format.raw/*7.87*/("""">
  <link rel="stylesheet" media="screen" href=""""),_display_(/*8.48*/routes/*8.54*/.Assets.versioned("lib/bootstrap/css/bootstrap.min.css")),format.raw/*8.110*/("""">
  <script src=""""),_display_(/*9.17*/assetsFinder/*9.29*/.path("javascripts/jquery.js")),format.raw/*9.59*/("""" type="text/javascript"></script>
  <script type="text/javascript" src=""""),_display_(/*10.40*/routes/*10.46*/.Assets.versioned("lib/react/umd/react.development.js")),format.raw/*10.101*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*11.40*/routes/*11.46*/.Assets.versioned("lib/react-dom/umd/react-dom.development.js")),format.raw/*11.109*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*12.40*/routes/*12.46*/.Assets.versioned("lib/crossfilter2/crossfilter.min.js")),format.raw/*12.102*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*13.40*/routes/*13.46*/.Assets.versioned("javascripts/scatterGraph.js")),format.raw/*13.94*/(""""></script>
   <script type="text/javascript" src=""""),_display_(/*14.41*/routes/*14.47*/.Assets.versioned("javascripts/configViewer.js")),format.raw/*14.95*/(""""></script>
  <script src="https://d3js.org/d3.v5.min.js"></script>
</head>
<body>
"""),_display_(/*18.2*/navbar(logoutUrl)),format.raw/*18.19*/("""
"""),format.raw/*19.1*/("""<div class="container">
  <div>
   """),_display_(/*21.5*/helper/*21.11*/.CSRF.formField),format.raw/*21.26*/("""
  """),format.raw/*22.3*/("""</div>
  <div id="root"></div>
  <div id="config"></div>
  <div id="speciesData"></div>
  <div class="row">
	</div>
  </div>
<script type='text/javascript' src='"""),_display_(/*29.38*/routes/*29.44*/.Assets.versioned("javascripts/scatterGrid.js")),format.raw/*29.91*/("""'></script>

<script src=""""),_display_(/*31.15*/assetsFinder/*31.27*/.path("javascripts/d3-v4.js")),format.raw/*31.56*/("""" type="text/javascript"></script>

</body>
</html>"""))
      }
    }
  }

  def render(message:String,logoutUrl:Call,assetsFinder:AssetsFinder,request:RequestHeader): play.twirl.api.HtmlFormat.Appendable = apply(message,logoutUrl)(assetsFinder,request)

  def f:((String,Call) => (AssetsFinder,RequestHeader) => play.twirl.api.HtmlFormat.Appendable) = (message,logoutUrl) => (assetsFinder,request) => apply(message,logoutUrl)(assetsFinder,request)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Tue Aug 14 20:03:21 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/train-engine/app/views/aggregateAnalysis.scala.html
                  HASH: afaad59fb513264fdf06bf1a3c0cd7b5dead7b1d
                  MATRIX: 773->1|963->96|993->100|1116->197|1136->209|1184->237|1261->288|1275->294|1352->350|1398->370|1418->382|1468->412|1570->487|1585->493|1662->548|1741->600|1756->606|1841->669|1920->721|1935->727|2013->783|2092->835|2107->841|2176->889|2256->942|2271->948|2340->996|2454->1084|2492->1101|2521->1103|2585->1141|2600->1147|2636->1162|2667->1166|2863->1335|2878->1341|2946->1388|3002->1417|3023->1429|3073->1458
                  LINES: 21->1|26->1|28->3|32->7|32->7|32->7|33->8|33->8|33->8|34->9|34->9|34->9|35->10|35->10|35->10|36->11|36->11|36->11|37->12|37->12|37->12|38->13|38->13|38->13|39->14|39->14|39->14|43->18|43->18|44->19|46->21|46->21|46->21|47->22|54->29|54->29|54->29|56->31|56->31|56->31
                  -- GENERATED --
              */
          