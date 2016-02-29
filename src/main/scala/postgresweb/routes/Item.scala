package postgresweb.routes

import japgolly.scalajs.react.ReactElement
import japgolly.scalajs.react.extra.router.RouterConfigDsl
import japgolly.scalajs.react.extra.router.StaticDsl.RouteB
import postgresweb.components.items.{Inserts, HomePage, Item2Data, Tables}

//import scalajsreact.template.pages.ItemsPage

sealed abstract class Item(val title: String,
val routerPath: String,
val model:String,
val render: () => ReactElement)

case class Model(model:String)


object Item {

  case object Home extends Item("Home","home","home",() => HomePage())


  case class Export(override val model:String) extends Item("Export","export",model,() => Item2Data())

  case class Table(override val model:String) extends Item("Table","table",model,() => Tables(model)())
  case class Insert(override val model:String) extends Item("Insert","insert",model,() => Inserts(model)())


  private val menu:Vector[Item] = Vector(Home)

  private def modelMenu(model:String):Vector[Item] = Vector(Table(model),Insert(model),Export(model))

  val routes = RouterConfigDsl[Item].buildRule { dsl =>
    import dsl._

    val table:RouteB[Table] = ("table" / string("(.+)$")).caseClass[Table]
    val insert:RouteB[Insert] = ("insert" / string("(.+)$")).caseClass[Insert]



    ( staticRoute("home",Home) ~> renderR(r => ItemsPage(props = ItemsPage.Props(menu,Home, r)))
    | dynamicRouteCT(table) ~> dynRenderR{ case(m,r) => ItemsPage(props = ItemsPage.Props(modelMenu(m.model),m, r))}
    | dynamicRouteCT(insert) ~> dynRenderR{ case(m,r) => ItemsPage(props = ItemsPage.Props(modelMenu(m.model),m, r))}
    )

  }

}
