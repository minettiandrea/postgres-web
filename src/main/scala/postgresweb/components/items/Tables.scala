package postgresweb.components.items

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ReactComponentB, _}
import postgresweb.components.base.{TableComponent, SchemaForm}
import postgresweb.css.CommonStyles
import postgresweb.models.{Table, JSONSchema, JSONQuery}
import postgresweb.services.ModelClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


case class Tables(model:String) {


  case class State(table:Table)

  class Backend(scope:BackendScope[Unit,State]) {



      val client = ModelClient(model)

      client.Helpers.filter2table(JSONQuery.baseFilter).onComplete{
        case Success(table) => scope.modState( _.copy(table = table)).runNow()
        case Failure(f) => {
          f.printStackTrace()
          println("Something went wrong: " + f)
        }
      }




    def render(s:State) = {
      <.div(CommonStyles.row,
        <.div(CommonStyles.fullWidth,TableComponent(TableComponent.Props(s.table,1)))
      )
    }
  }



  val component = ReactComponentB[Unit]("ItemsInfo")
    .initialState(State(Table.empty))
    .renderBackend[Backend]
    .buildU

  def apply() = component()
}