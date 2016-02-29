package postgresweb.services

import io.circe._
import io.circe.syntax._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

import postgresweb.configs.Path
import postgresweb.models._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by andreaminetti on 23/02/16.
  */
class ModelClient(model:String) {

  val paths = Path.forModel(model)

  def list(jsonQuery: JSONQuery) = HttpClient.postJson[JSONResponse](paths.list,jsonQuery.asJson.noSpaces)
  def schema = HttpClient.get[JSONSchema](paths.schema)
  def form = HttpClient.get[Vector[JSONField]](paths.form)
  def keys = HttpClient.get[Vector[String]](paths.keys)
  def count = HttpClient.get[JSONCount](paths.count)
  def get(i:Int) = HttpClient.get[Map[String,String]](paths.get(i))
  def update(i:Int,data:String) = HttpClient.putJson[String](paths.update(i),data)
  def insert(data:String) = HttpClient.postJson[String](paths.insert,data)
  def firsts = HttpClient.get[JSONResponse](paths.firsts)

  object Helpers {

    def filter2table(filter: JSONQuery): Future[Table] = {

      def valueForKey(key:String, map: Map[String,Json]):String = {
        for{
          result <- map.lift(key).map { el =>
            (el.asNumber,el.asString) match {
              case (Some(n),_) => n.toString
              case (_,Some(s)) => s
              case (_,_) => el.toString
            }
          }
        } yield result
      }.getOrElse("")

      for {
        f <- form
        result <- list(filter)
      } yield {
        val headers = f.map(_.key)
        val rows =
          result.data.map { row =>
            f.map { field =>
              valueForKey(field.key,row)
            }
          }
        Table(headers,rows)
      }
    }
  }

}

object ModelClient{
  def apply(model:String) = new ModelClient(model)

  def models() = HttpClient.get[Vector[String]](Path.models)

}
