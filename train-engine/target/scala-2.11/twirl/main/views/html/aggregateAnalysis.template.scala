
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
  <script src="https://d3js.org/d3.v5.min.js"></script>
</head>
<body>
"""),_display_(/*17.2*/navbar(logoutUrl)),format.raw/*17.19*/("""
"""),format.raw/*18.1*/("""<div class="container">
  <div>
   """),_display_(/*20.5*/helper/*20.11*/.CSRF.formField),format.raw/*20.26*/("""
  """),format.raw/*21.3*/("""</div>
  <div id="root"></div>
  <div id="speciesData"></div>
  <div class="row">
	</div>
  </div>
<script type='text/javascript' src='"""),_display_(/*27.38*/routes/*27.44*/.Assets.versioned("javascripts/scatterGrid.js")),format.raw/*27.91*/("""'></script>

<script src=""""),_display_(/*29.15*/assetsFinder/*29.27*/.path("javascripts/d3-v4.js")),format.raw/*29.56*/("""" type="text/javascript"></script>

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
                  DATE: Sun Aug 12 19:38:08 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/train-engine/app/views/aggregateAnalysis.scala.html
                  HASH: 61998aefd0aa607d5bc0dd01af2f8767dd325a1e
                  MATRIX: 773->1|963->96|993->100|1116->197|1136->209|1184->237|1261->288|1275->294|1352->350|1398->370|1418->382|1468->412|1570->487|1585->493|1662->548|1741->600|1756->606|1841->669|1920->721|1935->727|2013->783|2092->835|2107->841|2176->889|2290->977|2328->994|2357->996|2421->1034|2436->1040|2472->1055|2503->1059|2672->1201|2687->1207|2755->1254|2811->1283|2832->1295|2882->1324
                  LINES: 21->1|26->1|28->3|32->7|32->7|32->7|33->8|33->8|33->8|34->9|34->9|34->9|35->10|35->10|35->10|36->11|36->11|36->11|37->12|37->12|37->12|38->13|38->13|38->13|42->17|42->17|43->18|45->20|45->20|45->20|46->21|52->27|52->27|52->27|54->29|54->29|54->29
                  -- GENERATED --
              */
          