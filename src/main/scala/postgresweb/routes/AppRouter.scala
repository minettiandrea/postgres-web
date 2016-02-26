package postgresweb.routes

import japgolly.scalajs.react.extra.router.{Resolution, RouterConfigDsl, RouterCtl, _}
import japgolly.scalajs.react.vdom.prefix_<^._
import postgresweb.css.CommonStyles
import postgresweb.models.Menu

object AppRouter {

  sealed trait AppPage

  case class Items(p : Item) extends AppPage


  val config = RouterConfigDsl[AppPage].buildConfig { dsl =>
    import dsl._

    val itemRoutes : Rule = Item.routes.prefixPath_/("#items").pmap[AppPage](Items){ case Items(p) => p}

    (trimSlashes
      | itemRoutes
      ).notFound(redirectToPage(Items(Item.Item1))(Redirect.Replace))
      .renderWith(layout)
  }



  def layout(c: RouterCtl[AppPage], r: Resolution[AppPage]) = {
      <.div( //fix for Material Design Light
        r.render()
      )
  }

  val baseUrl = BaseUrl.fromWindowOrigin

  val router = Router(baseUrl, config)

}
