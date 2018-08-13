
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
/*2.2*/import com.thingy.config.ConfigDataClass.ConfigData

object config extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template5[Form[ConfigData],Call,Messages,AssetsFinder,RequestHeader,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*4.2*/(configForm: Form[ConfigData], logoutUrl: Call)(implicit messages: Messages, assetsFinder: AssetsFinder, request: RequestHeader):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*4.130*/("""


"""),format.raw/*7.1*/("""<!DOCTYPE html>
<html>
<head>
	<title></title>
	<link rel="stylesheet" media="screen" href=""""),_display_(/*11.47*/assetsFinder/*11.59*/.path("stylesheets/app.css")),format.raw/*11.87*/("""">
  <link rel="stylesheet" media="screen" href=""""),_display_(/*12.48*/routes/*12.54*/.Assets.versioned("lib/bootstrap/css/bootstrap.min.css")),format.raw/*12.110*/("""">
  <script src=""""),_display_(/*13.17*/assetsFinder/*13.29*/.path("javascripts/jquery.js")),format.raw/*13.59*/("""" type="text/javascript"></script>
  <script type="text/javascript" src=""""),_display_(/*14.40*/routes/*14.46*/.Assets.versioned("lib/react-dom/umd/react-dom.development.js")),format.raw/*14.109*/(""""></script>
  <script type="text/javascript" src=""""),_display_(/*15.40*/routes/*15.46*/.Assets.versioned("lib/crossfilter2/crossfilter.min.js")),format.raw/*15.102*/(""""></script>
</head>
<body>
"""),_display_(/*18.2*/navbar(logoutUrl)),format.raw/*18.19*/("""
"""),format.raw/*19.1*/("""<div class="container">
<h1>Config </h1>
  <div>
  
  """),_display_(/*23.4*/helper/*23.10*/.form(routes.ConfigController.userPost())/*23.51*/ {_display_(Seq[Any](format.raw/*23.53*/("""

  """),_display_(/*25.4*/helper/*25.10*/.CSRF.formField),format.raw/*25.25*/("""

  """),format.raw/*27.3*/("""<div class="form-group">
    """),_display_(/*28.6*/helper/*28.12*/.inputText(configForm("populationSize"), 'class -> "form-control")),format.raw/*28.78*/("""
 """),format.raw/*29.2*/("""</div>
 <div class="form-group">
  """),_display_(/*31.4*/helper/*31.10*/.inputText(configForm("maxGenerations"), 'class -> "form-control")),format.raw/*31.76*/("""
  """),format.raw/*32.3*/("""</div>
 <div class="form-group">
  """),_display_(/*34.4*/helper/*34.10*/.inputText(configForm("connectionWeightRange"), 'class -> "form-control")),format.raw/*34.83*/("""
  """),format.raw/*35.3*/("""</div>
 <div class="form-group">
  """),_display_(/*37.4*/helper/*37.10*/.inputText(configForm("speciesMembershipThreshold"), 'class -> "form-control")),format.raw/*37.88*/("""
  """),format.raw/*38.3*/("""</div>
 <div class="form-group">
  """),_display_(/*40.4*/helper/*40.10*/.inputText(configForm("crossoverRate"), 'class -> "form-control")),format.raw/*40.75*/("""
  """),format.raw/*41.3*/("""</div>
 <div class="form-group">
  """),_display_(/*43.4*/helper/*43.10*/.inputText(configForm("globalMutationRate"), 'class -> "form-control")),format.raw/*43.80*/("""
  """),format.raw/*44.3*/("""</div>
 <div class="form-group">
  """),_display_(/*46.4*/helper/*46.10*/.inputText(configForm("weightMutationRate"), 'class -> "form-control")),format.raw/*46.80*/("""
  """),format.raw/*47.3*/("""</div>
 <div class="form-group">
  """),_display_(/*49.4*/helper/*49.10*/.inputText(configForm("weightJiggleOverReset"), 'class -> "form-control")),format.raw/*49.83*/("""
  """),format.raw/*50.3*/("""</div>
  <button type="submit" class="btn btn-primary">Submit</button>
""")))}),format.raw/*52.2*/("""
  """),format.raw/*53.3*/("""</div>
</div>
<script type='text/javascript' src='"""),_display_(/*55.38*/routes/*55.44*/.Assets.versioned("javascripts/index.js")),format.raw/*55.85*/("""'></script>

<script src=""""),_display_(/*57.15*/assetsFinder/*57.27*/.path("javascripts/d3-v4.js")),format.raw/*57.56*/("""" type="text/javascript"></script>
<script src=""""),_display_(/*58.15*/assetsFinder/*58.27*/.path("javascripts/app.js")),format.raw/*58.54*/("""" type="text/javascript"></script>
</body>
</html>"""))
      }
    }
  }

  def render(configForm:Form[ConfigData],logoutUrl:Call,messages:Messages,assetsFinder:AssetsFinder,request:RequestHeader): play.twirl.api.HtmlFormat.Appendable = apply(configForm,logoutUrl)(messages,assetsFinder,request)

  def f:((Form[ConfigData],Call) => (Messages,AssetsFinder,RequestHeader) => play.twirl.api.HtmlFormat.Appendable) = (configForm,logoutUrl) => (messages,assetsFinder,request) => apply(configForm,logoutUrl)(messages,assetsFinder,request)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Sun Aug 12 10:00:30 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/train-engine/app/views/config.scala.html
                  HASH: b9092c01dc36b7cc765821b8e3cb07a99493c501
                  MATRIX: 432->3|840->59|1064->187|1096->193|1220->290|1241->302|1290->330|1368->381|1383->387|1461->443|1508->463|1529->475|1580->505|1682->580|1697->586|1782->649|1861->701|1876->707|1954->763|2011->794|2049->811|2078->813|2163->872|2178->878|2228->919|2268->921|2301->928|2316->934|2352->949|2385->955|2442->986|2457->992|2544->1058|2574->1061|2638->1099|2653->1105|2740->1171|2771->1175|2835->1213|2850->1219|2944->1292|2975->1296|3039->1334|3054->1340|3153->1418|3184->1422|3248->1460|3263->1466|3349->1531|3380->1535|3444->1573|3459->1579|3550->1649|3581->1653|3645->1691|3660->1697|3751->1767|3782->1771|3846->1809|3861->1815|3955->1888|3986->1892|4090->1966|4121->1970|4201->2023|4216->2029|4278->2070|4334->2099|4355->2111|4405->2140|4482->2190|4503->2202|4551->2229
                  LINES: 17->2|22->4|27->4|30->7|34->11|34->11|34->11|35->12|35->12|35->12|36->13|36->13|36->13|37->14|37->14|37->14|38->15|38->15|38->15|41->18|41->18|42->19|46->23|46->23|46->23|46->23|48->25|48->25|48->25|50->27|51->28|51->28|51->28|52->29|54->31|54->31|54->31|55->32|57->34|57->34|57->34|58->35|60->37|60->37|60->37|61->38|63->40|63->40|63->40|64->41|66->43|66->43|66->43|67->44|69->46|69->46|69->46|70->47|72->49|72->49|72->49|73->50|75->52|76->53|78->55|78->55|78->55|80->57|80->57|80->57|81->58|81->58|81->58
                  -- GENERATED --
              */
          