
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

object navbar extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template1[Call,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(logoutUrl: Call):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.19*/("""
"""),format.raw/*2.1*/("""<nav class="navbar navbar-expand-lg navbar-light bg-light">
			  <a class="navbar-brand" href="#">thingy</a>
			  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
			    <span class="navbar-toggler-icon"></span>
			  </button>
			  <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
			    <div class="navbar-nav">
			      <a class="nav-item nav-link active" href="/netviewer">Results <span class="sr-only">(current)</span></a>
			      <a class="nav-item nav-link" href="/config">Setup New</a>
			      <a class="nav-item nav-link" href="/aggregateAnalysis">Parameter Correlation</a>
			      <a class="nav-item nav-link" href=""""),_display_(/*12.46*/logoutUrl),format.raw/*12.55*/("""">logout</a>
			    </div>
			  </div>
			</nav>"""))
      }
    }
  }

  def render(logoutUrl:Call): play.twirl.api.HtmlFormat.Appendable = apply(logoutUrl)

  def f:((Call) => play.twirl.api.HtmlFormat.Appendable) = (logoutUrl) => apply(logoutUrl)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Mon Aug 13 22:42:51 BST 2018
                  SOURCE: C:/Users/mau/Documents/thingy/train-engine/app/views/navbar.scala.html
                  HASH: ffb26e99fb171b22c417804da88959b78dafd653
                  MATRIX: 728->1|840->18|868->20|1690->815|1720->824
                  LINES: 21->1|26->1|27->2|37->12|37->12
                  -- GENERATED --
              */
          