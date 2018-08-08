
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

object userLogin extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template3[Form[models.User],Call,MessagesRequestHeader,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(
    form: Form[models.User],
    postUrl: Call
)(implicit request: MessagesRequestHeader):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*4.43*/("""

"""),format.raw/*6.1*/("""<!DOCTYPE html>
<html lang="en">
<head>
      <link rel="stylesheet" media="screen" href=""""),_display_(/*9.52*/routes/*9.58*/.Assets.versioned("lib/bootstrap/css/bootstrap.min.css")),format.raw/*9.114*/("""">
</head>

<body id="user-login">
<div id="content">

    <div id="user-login-form">

        <h1>User Login</h1>

        """),_display_(/*19.10*/request/*19.17*/.flash.data.map/*19.32*/{ case (name, value) =>_display_(Seq[Any](format.raw/*19.55*/("""
            """),format.raw/*20.13*/("""<div>"""),_display_(/*20.19*/name),format.raw/*20.23*/(""": """),_display_(/*20.26*/value),format.raw/*20.31*/("""</div>
        """)))}),format.raw/*21.10*/("""

        """),format.raw/*23.70*/("""
        """),_display_(/*24.10*/if(form.hasGlobalErrors)/*24.34*/ {_display_(Seq[Any](format.raw/*24.36*/("""
            """),_display_(/*25.14*/form/*25.18*/.globalErrors.map/*25.35*/ { error: FormError =>_display_(Seq[Any](format.raw/*25.57*/("""
                """),format.raw/*26.17*/("""<div>
                    Error: """),_display_(/*27.29*/error/*27.34*/.key),format.raw/*27.38*/(""": """),_display_(/*27.41*/error/*27.46*/.message),format.raw/*27.54*/("""
                """),format.raw/*28.17*/("""</div>
            """)))}),format.raw/*29.14*/("""
        """)))}),format.raw/*30.10*/("""

        """),_display_(/*32.10*/helper/*32.16*/.form(postUrl, 'id -> "user-login-form")/*32.56*/ {_display_(Seq[Any](format.raw/*32.58*/("""

        """),_display_(/*34.10*/helper/*34.16*/.CSRF.formField),format.raw/*34.31*/("""

        """),_display_(/*36.10*/helper/*36.16*/.inputText(
            form("username"),
            '_label -> "Username",
            'placeholder -> "username",
            'id -> "username",
            'size -> 60
        )),format.raw/*42.10*/("""

        """),_display_(/*44.10*/helper/*44.16*/.inputPassword(
            form("password"),
            '_label -> "Password",
            'placeholder -> "password",
            'id -> "password",
            'size -> 60
        )),format.raw/*50.10*/("""

        """),format.raw/*52.9*/("""<button>Login</button>

        """)))}),format.raw/*54.10*/("""

    """),format.raw/*56.5*/("""</div>

</div>

</body>
</html>

"""))
      }
    }
  }

  def render(form:Form[models.User],postUrl:Call,request:MessagesRequestHeader): play.twirl.api.HtmlFormat.Appendable = apply(form,postUrl)(request)

  def f:((Form[models.User],Call) => (MessagesRequestHeader) => play.twirl.api.HtmlFormat.Appendable) = (form,postUrl) => (request) => apply(form,postUrl)(request)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Mon Jul 23 23:01:18 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/train-engine/app/views/userLogin.scala.html
                  HASH: 099ab350b48729bf7482eeccae6a5b797ea81519
                  MATRIX: 771->1|960->95|990->99|1110->193|1124->199|1201->255|1363->390|1379->397|1403->412|1464->435|1506->449|1539->455|1564->459|1594->462|1620->467|1668->484|1708->557|1746->568|1779->592|1819->594|1861->609|1874->613|1900->630|1960->652|2006->670|2068->705|2082->710|2107->714|2137->717|2151->722|2180->730|2226->748|2278->769|2320->780|2360->793|2375->799|2424->839|2464->841|2504->854|2519->860|2555->875|2595->888|2610->894|2818->1081|2858->1094|2873->1100|3085->1291|3124->1303|3190->1338|3225->1346
                  LINES: 21->1|29->4|31->6|34->9|34->9|34->9|44->19|44->19|44->19|44->19|45->20|45->20|45->20|45->20|45->20|46->21|48->23|49->24|49->24|49->24|50->25|50->25|50->25|50->25|51->26|52->27|52->27|52->27|52->27|52->27|52->27|53->28|54->29|55->30|57->32|57->32|57->32|57->32|59->34|59->34|59->34|61->36|61->36|67->42|69->44|69->44|75->50|77->52|79->54|81->56
                  -- GENERATED --
              */
          