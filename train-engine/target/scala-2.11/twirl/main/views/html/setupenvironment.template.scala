
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

object setupenvironment extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[Call,AssetsFinder,play.twirl.api.HtmlFormat.Appendable] {

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

<div class="container">
	<form id="setupform">
		<label>Name the environment</label><input type="text" />
		<div class="custom-file">
  			<input type="file" class="custom-file-input" id="customFile">
  			<label class="custom-file-label" for="customFile">Choose file</label>
		</div>
	</form>
</div>
<script src=""""),_display_(/*26.15*/assetsFinder/*26.27*/.path("javascripts/fileupload.js")),format.raw/*26.61*/("""" type="text/javascript">

</script>
<script src=""""),_display_(/*29.15*/assetsFinder/*29.27*/.path("javascripts/websocketprogress.js")),format.raw/*29.68*/("""" type="text/javascript"></script>
<script src=""""),_display_(/*30.15*/assetsFinder/*30.27*/.path("javascripts/d3-v4.js")),format.raw/*30.56*/("""" type="text/javascript"></script>
</body>
</html"""))
      }
    }
  }

  def render(logoutUrl:Call,assetsFinder:AssetsFinder): play.twirl.api.HtmlFormat.Appendable = apply(logoutUrl)(assetsFinder)

  def f:((Call) => (AssetsFinder) => play.twirl.api.HtmlFormat.Appendable) = (logoutUrl) => (assetsFinder) => apply(logoutUrl)(assetsFinder)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Mon Aug 20 22:32:10 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/train-engine/app/views/setupenvironment.scala.html
                  HASH: f2672804d5086366907a15d7ad4f04da411aad2b
                  MATRIX: 751->1|900->55|930->59|1053->156|1073->168|1121->196|1198->247|1212->253|1289->309|1335->329|1355->341|1405->371|1513->452|1528->458|1613->521|1692->573|1707->579|1785->635|2168->991|2189->1003|2244->1037|2325->1091|2346->1103|2408->1144|2485->1194|2506->1206|2556->1235
                  LINES: 21->1|26->1|28->3|32->7|32->7|32->7|33->8|33->8|33->8|34->9|34->9|34->9|37->12|37->12|37->12|38->13|38->13|38->13|51->26|51->26|51->26|54->29|54->29|54->29|55->30|55->30|55->30
                  -- GENERATED --
              */
          