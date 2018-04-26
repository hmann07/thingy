
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

object netViewer extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,AssetsFinder,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*2.2*/(message: String)(implicit assetsFinder: AssetsFinder):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.56*/("""

"""),format.raw/*4.1*/("""<!DOCTYPE html>
<html>
<head>
	<title></title>
	<link rel="stylesheet" media="screen" href=""""),_display_(/*8.47*/assetsFinder/*8.59*/.path("stylesheets/app.css")),format.raw/*8.87*/("""">
  <link rel="stylesheet" media="screen" href=""""),_display_(/*9.48*/routes/*9.54*/.Assets.versioned("lib/bootstrap/css/bootstrap.min.css")),format.raw/*9.110*/("""">
  <script src=""""),_display_(/*10.17*/assetsFinder/*10.29*/.path("javascripts/jquery.js")),format.raw/*10.59*/("""" type="text/javascript"></script>
  <script data-main=""""),_display_(/*11.23*/routes/*11.29*/.Assets.versioned("javascripts/index.js")),format.raw/*11.70*/("""" type="text/javascript" src=""""),_display_(/*11.101*/routes/*11.107*/.Assets.versioned("lib/react/umd/react.development.js")),format.raw/*11.162*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*12.40*/routes/*12.46*/.Assets.versioned("lib/react-dom/umd/react-dom.development.js")),format.raw/*12.109*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*13.40*/routes/*13.46*/.Assets.versioned("lib/crossfilter2/crossfilter.min.js")),format.raw/*13.102*/(""""></script>
</head>
<body>

<div class="container">
<h1>Welcome </h1>
  <a href="/start">Start evolving</a>
  <a href="/stop">Stop evolving</a>
  <a href="/viewgenome">View Genomes</a>
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

  def render(message:String,assetsFinder:AssetsFinder): play.twirl.api.HtmlFormat.Appendable = apply(message)(assetsFinder)

  def f:((String) => (AssetsFinder) => play.twirl.api.HtmlFormat.Appendable) = (message) => (assetsFinder) => apply(message)(assetsFinder)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Wed Apr 25 21:53:44 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/app/views/netViewer.scala.html
                  HASH: 2fcb09132c6b51208304fbe5232aad09629cf867
                  MATRIX: 746->3|895->57|925->61|1048->158|1068->170|1116->198|1193->249|1207->255|1284->311|1331->331|1352->343|1403->373|1488->431|1503->437|1565->478|1624->509|1640->515|1717->570|1796->622|1811->628|1896->691|1975->743|1990->749|2068->805|2490->1200|2505->1206|2567->1247|2623->1276|2644->1288|2694->1317|2771->1367|2792->1379|2840->1406
                  LINES: 21->2|26->2|28->4|32->8|32->8|32->8|33->9|33->9|33->9|34->10|34->10|34->10|35->11|35->11|35->11|35->11|35->11|35->11|36->12|36->12|36->12|37->13|37->13|37->13|54->30|54->30|54->30|56->32|56->32|56->32|57->33|57->33|57->33
                  -- GENERATED --
              */
          