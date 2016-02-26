package postgresweb.routes

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import postgresweb.components.{LeftNav, TopNav}
import postgresweb.css.CommonStyles

import scala.scalajs.js
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object ItemsPage {

  object Style extends StyleSheet.Inline {
    import dsl._
    val content = style(addClassNames("mdl-layout__content"))
    val pageContent = style(addClassNames("page-content"))
  }

  val component = ReactComponentB[Props]("ItemsPage")
    .render_P { P =>
      <.div(CommonStyles.layout,
        TopNav(TopNav.Props(P.menu,P.selectedPage,P.ctrl)),
        LeftNav(LeftNav.Props("Tables",P.ctrl)),
        <.main(Style.content,
          <.div(Style.pageContent,
            <.div(CommonStyles.row,
              <.div(CommonStyles.fullWidth,
                P.selectedPage.render()
              )
            )
          )
        )
      )
    }
    .build

  case class Props(menu:Vector[Item],selectedPage : Item,ctrl : RouterCtl[Item])

  def apply(props : Props,ref: js.UndefOr[String] = "", key: js.Any = {}) = component.set(key, ref)(props)

}
