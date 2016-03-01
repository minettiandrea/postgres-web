package postgresweb.css

import postgresweb.components.base.TableComponent

import scalacss.ScalaCssReact._
import scalacss.mutable.GlobalRegistry
import scalacss.Defaults._

object AppCSS {

  def load = {
    GlobalRegistry.register(
      GlobalStyle,
      TableComponent.Style,
      CommonStyles
    )
    GlobalRegistry.onRegistration(_.addToDocument())
  }
}

