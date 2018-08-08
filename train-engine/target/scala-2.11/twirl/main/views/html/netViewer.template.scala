
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

object netViewer extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template3[String,Call,AssetsFinder,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(message: String, logoutUrl: Call)(implicit assetsFinder: AssetsFinder):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.73*/("""

"""),format.raw/*3.1*/("""<!DOCTYPE html>
<html>
<head>
	<title></title>
	<link rel="stylesheet" media="screen" href=""""),_display_(/*7.47*/assetsFinder/*7.59*/.path("stylesheets/app.css")),format.raw/*7.87*/("""">
  <link rel="stylesheet" media="screen" href=""""),_display_(/*8.48*/routes/*8.54*/.Assets.versioned("lib/bootstrap/css/bootstrap.min.css")),format.raw/*8.110*/("""">
  <script src=""""),_display_(/*9.17*/assetsFinder/*9.29*/.path("javascripts/jquery.js")),format.raw/*9.59*/("""" type="text/javascript"></script>
  <script data-main=""""),_display_(/*10.23*/routes/*10.29*/.Assets.versioned("javascripts/index.js")),format.raw/*10.70*/("""" type="text/javascript" src=""""),_display_(/*10.101*/routes/*10.107*/.Assets.versioned("lib/react/umd/react.development.js")),format.raw/*10.162*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*11.40*/routes/*11.46*/.Assets.versioned("lib/react-dom/umd/react-dom.development.js")),format.raw/*11.109*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*12.40*/routes/*12.46*/.Assets.versioned("lib/crossfilter2/crossfilter.min.js")),format.raw/*12.102*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*13.40*/routes/*13.46*/.Assets.versioned("javascripts/barchart.js")),format.raw/*13.90*/(""""></script>
  <script src="https://d3js.org/d3.v5.min.js"></script>
</head>
<body>
"""),_display_(/*17.2*/navbar(logoutUrl)),format.raw/*17.19*/("""
"""),format.raw/*18.1*/("""<div class="container">

  <div id="root"></div>
  <div id="speciesData"></div>
  <div class="row">
  	<div class ="col-9">
		<div id="networkViewer"></div>
	</div>
  </div>
</div>
<script type='text/javascript' src='"""),_display_(/*28.38*/routes/*28.44*/.Assets.versioned("javascripts/index.js")),format.raw/*28.85*/("""'></script>

<script src=""""),_display_(/*30.15*/assetsFinder/*30.27*/.path("javascripts/d3-v4.js")),format.raw/*30.56*/("""" type="text/javascript"></script>
<script src=""""),_display_(/*31.15*/assetsFinder/*31.27*/.path("javascripts/app.js")),format.raw/*31.54*/("""" type="text/javascript"></script>
</body>
</html>"""))
      }
    }
  }

  def render(message:String,logoutUrl:Call,assetsFinder:AssetsFinder): play.twirl.api.HtmlFormat.Appendable = apply(message,logoutUrl)(assetsFinder)

  def f:((String,Call) => (AssetsFinder) => play.twirl.api.HtmlFormat.Appendable) = (message,logoutUrl) => (assetsFinder) => apply(message,logoutUrl)(assetsFinder)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Mon Jul 23 23:07:06 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/train-engine/app/views/netViewer.scala.html
                  HASH: b1fd9c627597ae67ebfbd6ef688bcd419b483f12
                  MATRIX: 751->1|917->72|947->76|1070->173|1090->185|1138->213|1215->264|1229->270|1306->326|1352->346|1372->358|1422->388|1507->446|1522->452|1584->493|1643->524|1659->530|1736->585|1815->637|1830->643|1915->706|1994->758|2009->764|2087->820|2166->872|2181->878|2246->922|2360->1010|2398->1027|2427->1029|2682->1257|2697->1263|2759->1304|2815->1333|2836->1345|2886->1374|2963->1424|2984->1436|3032->1463
                  LINES: 21->1|26->1|28->3|32->7|32->7|32->7|33->8|33->8|33->8|34->9|34->9|34->9|35->10|35->10|35->10|35->10|35->10|35->10|36->11|36->11|36->11|37->12|37->12|37->12|38->13|38->13|38->13|42->17|42->17|43->18|53->28|53->28|53->28|55->30|55->30|55->30|56->31|56->31|56->31
                  -- GENERATED --
              */
          