
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

object netViewer extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template4[String,Call,AssetsFinder,RequestHeader,play.twirl.api.HtmlFormat.Appendable] {

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
  <script data-main=""""),_display_(/*10.23*/routes/*10.29*/.Assets.versioned("javascripts/index.js")),format.raw/*10.70*/("""" type="text/javascript" src=""""),_display_(/*10.101*/routes/*10.107*/.Assets.versioned("lib/react/umd/react.development.js")),format.raw/*10.162*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*11.40*/routes/*11.46*/.Assets.versioned("lib/react-dom/umd/react-dom.development.js")),format.raw/*11.109*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*12.40*/routes/*12.46*/.Assets.versioned("lib/crossfilter2/crossfilter.min.js")),format.raw/*12.102*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*13.40*/routes/*13.46*/.Assets.versioned("javascripts/barchart.js")),format.raw/*13.90*/(""""></script>
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
  	<div class ="col-9">
		<div id="networkViewer"></div>
	</div>
  </div>
</div>
<script type='text/javascript' src='"""),_display_(/*30.38*/routes/*30.44*/.Assets.versioned("javascripts/index.js")),format.raw/*30.85*/("""'></script>

<script src=""""),_display_(/*32.15*/assetsFinder/*32.27*/.path("javascripts/d3-v4.js")),format.raw/*32.56*/("""" type="text/javascript"></script>
<script src=""""),_display_(/*33.15*/assetsFinder/*33.27*/.path("javascripts/app.js")),format.raw/*33.54*/("""" type="text/javascript"></script>
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
                  DATE: Thu Aug 09 22:52:46 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/train-engine/app/views/netViewer.scala.html
                  HASH: 6cbe3be8b7dd1f93ad0e04504df20b61109f80a4
                  MATRIX: 765->1|955->96|985->100|1108->197|1128->209|1176->237|1253->288|1267->294|1344->350|1390->370|1410->382|1460->412|1545->470|1560->476|1622->517|1681->548|1697->554|1774->609|1853->661|1868->667|1953->730|2032->782|2047->788|2125->844|2204->896|2219->902|2284->946|2398->1034|2436->1051|2465->1053|2529->1091|2544->1097|2580->1112|2611->1116|2847->1325|2862->1331|2924->1372|2980->1401|3001->1413|3051->1442|3128->1492|3149->1504|3197->1531
                  LINES: 21->1|26->1|28->3|32->7|32->7|32->7|33->8|33->8|33->8|34->9|34->9|34->9|35->10|35->10|35->10|35->10|35->10|35->10|36->11|36->11|36->11|37->12|37->12|37->12|38->13|38->13|38->13|42->17|42->17|43->18|45->20|45->20|45->20|46->21|55->30|55->30|55->30|57->32|57->32|57->32|58->33|58->33|58->33
                  -- GENERATED --
              */
          