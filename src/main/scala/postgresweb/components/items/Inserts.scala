package postgresweb.components.items

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ReactComponentB, _}
import postgresweb.components.base.{TableComponent, SchemaForm}
import postgresweb.css.CommonStyles
import postgresweb.models._
import postgresweb.services.ModelClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


case class Inserts(model:String) {


  case class State(schema:JSONSchema, ui:JSONSchemaUI)

  class Backend(scope:BackendScope[Unit,State]) {



    val client = ModelClient(model)


    for{
      schema <- client.schema
      form <- client.form
    } yield {
      scope.modState(_.copy(schema = schema, ui = JSONSchemaUI.fromJSONFields(form))).runNow()
    }



    def render(s:State) = {
      <.div(CommonStyles.row,
        <.div(CommonStyles.fullWidth,SchemaForm(SchemaForm.Props(s.schema,s.ui)))
      )
    }
  }



  val component = ReactComponentB[Unit]("ItemsInfo")
    .initialState(State(JSONSchema.empty,JSONSchemaUI.empty))
    .renderBackend[Backend]
    .buildU

  def apply() = component()
}
