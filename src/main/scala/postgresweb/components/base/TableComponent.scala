package postgresweb.components.base

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.extra.Reusability
import japgolly.scalajs.react.vdom.prefix_<^._
import postgresweb.models.Table

import scala.scalajs.js.{Any, UndefOr}
import scalacss.Defaults._
import scalacss.ScalaCssReact._

/**
 * Created by andreaminetti on 22/02/16.
 */
object TableComponent {
  object Style extends StyleSheet.Inline {

    import dsl._
    val table = style(addClassNames("mdl-data-table","mdl-js-data-table","mdl-shadow--2dp"))
    val td = style(addClassNames("mdl-data-table__cell--non-numeric"))
  }

  implicit val propsReuse = Reusability.fn[Props]{ case (x,y) => x.page == y.page && x.table.rows.length == y.table.rows.length && x.table.headers.length == y.table.headers.length }

  case class Props(table:Table,page:Int)

  val component = ReactComponentB[Props]("Table")
    .render_P { P =>
      <.table(Style.table,
        <.thead(
          <.tr(
            P.table.headers.map(title => <.th(Style.td,title))
          )
        ),
        <.tbody(
          P.table.rows.map{row =>
            <.tr(
              row.map{ cell =>
                <.td(Style.td,cell.toString)
              }
            )
          }
        )
      )
    }
    .configure(Reusability.shouldComponentUpdate)
    .build


  def apply(props: Props, ref: UndefOr[String] = "", key: Any = {}) = component.set(key, ref)(props)

}
