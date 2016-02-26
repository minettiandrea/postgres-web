package postgresweb.routes

import japgolly.scalajs.react.ReactElement
import japgolly.scalajs.react.extra.router.RouterConfigDsl
import japgolly.scalajs.react.extra.router.StaticDsl.RouteB
import postgresweb.components.items.{Item1Data, Item2Data, Tables}

//import scalajsreact.template.pages.ItemsPage

sealed abstract class Item(val title: String,
val routerPath: String,
val render: () => ReactElement)

case class Model(model:String)


object Item {

  case object Item1 extends Item("Insert","insert",() => Item1Data())

  case object Item2 extends Item("Update","update",() => Item2Data())

  case object Export extends Item("Export","export",() => Item2Data())

  case class Table(model:String) extends Item("Table","table",() => Tables(model)())

  private val menu = Vector(Item1,Item2, Export)

  val routes = RouterConfigDsl[Item].buildRule { dsl =>
    import dsl._

    val r:RouteB[Table] = ("table" / string("(.+)$")).caseClass[Table]


    val result = menu.map { i =>
      staticRoute(i.routerPath,i) ~> renderR(r => ItemsPage(props = ItemsPage.Props(menu,i, r)))
    }.reduce(_ | _)

    result | dynamicRouteCT(r) ~> dynRenderR{ case(m,r) => ItemsPage(props = ItemsPage.Props((m :: menu.toList).toVector,m, r))}

  }

}
