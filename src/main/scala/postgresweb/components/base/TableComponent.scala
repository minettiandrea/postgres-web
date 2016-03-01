package postgresweb.components.base

import japgolly.scalajs.react.{Callback, BackendScope, ReactComponentB}
import japgolly.scalajs.react.extra.Reusability
import japgolly.scalajs.react.vdom.prefix_<^._
import postgresweb.css.CommonStyles
import postgresweb.models.Table
import postgresweb.services.GlobalState

import scala.scalajs.js.{Any, UndefOr}
import scala.util.Try
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

    val select = style(addClassName("select-wrap"))
    val input  = style(
      addClassName("mdl-textfield__input"),
      width(100.px),
      fontSize(11.px),
      display.inherit,
      lineHeight(15.px)
    )

    val selected = styleF.bool( selected => styleS(
        mixinIf(selected)(
          backgroundColor(c"#C5CAE9")
        )
      )
    )
  }

  implicit val propsReuse = Reusability.fn[Props]{ case (x,y) => x.page == y.page && x.table.rows.length == y.table.rows.length && x.table.headers.length == y.table.headers.length }

  case class Props(table:Table,page:Int)

  case class State(selectedRow:Vector[(String,String)])

  class Backend(scope:BackendScope[Props,State]) {

    def selectRow(headers: Vector[String], row: Vector[String]) = {
      GlobalState.selectedId = Try{row.head.toInt}.toOption
      for{
        _ <- Callback.log("Selected row: " + row)
        result <- scope.modState(_.copy(selectedRow = headers.zip(row)))
      } yield result
    }

    def render(P:Props,S:State) = {
      <.div(CommonStyles.scroll,
        <.table(Style.table,
          <.thead(
            <.tr(
              P.table.headers.map(title => <.th(Style.td,title))
            )
          ),
          <.tbody(
            <.tr(
              P.table.headers.map(title => <.td(Style.td,
                <.input(Style.input),
                <.span(Style.select,
                  <.select(
                    <.option("test")
                  )
                )
              ))
            ),
            P.table.rows.map{row =>
              <.tr( Style.selected(row == S.selectedRow.map(_._2)),
                ^.onClick --> selectRow(P.table.headers,row),
                row.map{ cell =>
                  <.td(Style.td,cell.toString)
                }
              )
            }
          )
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Table")
    .initialState(State(Vector()))
    .renderBackend[Backend]
    .build


  def apply(props: Props, ref: UndefOr[String] = "", key: Any = {}) = component.set(key, ref)(props)

}
