
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

object loginLandingPage extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[Call,Flash,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(logoutUrl: Call)(implicit flash: Flash):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.42*/("""

	"""),format.raw/*3.2*/("""<html>
	<head>
		  <link rel="stylesheet" media="screen" href=""""),_display_(/*5.50*/routes/*5.56*/.Assets.versioned("lib/bootstrap/css/bootstrap.min.css")),format.raw/*5.112*/("""">
	</head>
	<body>
		<div>
			"""),_display_(/*9.5*/navbar(logoutUrl)),format.raw/*9.22*/("""
    	"""),format.raw/*10.6*/("""<h1>Welcome """),_display_(/*10.19*/flash/*10.24*/.get("info").getOrElse("")),format.raw/*10.50*/("""</h1>

    	"""),format.raw/*12.45*/("""
    	"""),_display_(/*13.7*/flash/*13.12*/.get("info").getOrElse("")),format.raw/*13.38*/("""

    	"""),format.raw/*15.6*/("""<div>
        	
    	</div>
    </div>
	</body>
    </html>"""))
      }
    }
  }

  def render(logoutUrl:Call,flash:Flash): play.twirl.api.HtmlFormat.Appendable = apply(logoutUrl)(flash)

  def f:((Call) => (Flash) => play.twirl.api.HtmlFormat.Appendable) = (logoutUrl) => (flash) => apply(logoutUrl)(flash)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Mon Jul 23 23:01:18 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/train-engine/app/views/loginLandingPage.scala.html
                  HASH: 222c547e51aa2417f1c010117481ae1bf4b05797
                  MATRIX: 744->1|879->41|910->46|1002->112|1016->118|1093->174|1154->210|1191->227|1225->234|1265->247|1279->252|1326->278|1368->331|1402->339|1416->344|1463->370|1499->379
                  LINES: 21->1|26->1|28->3|30->5|30->5|30->5|34->9|34->9|35->10|35->10|35->10|35->10|37->12|38->13|38->13|38->13|40->15
                  -- GENERATED --
              */
          