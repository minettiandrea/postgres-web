package postgresweb.services

import io.circe._
import io.circe.syntax._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

import postgresweb.configs.Config
import postgresweb.models._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by andreaminetti on 23/02/16.
  */
class ModelClient(model:String) {

  object Paths {
    def list = Config.endpoint + model + "/list"
    def schema = Config.endpoint + model + "/schema"
    def form = Config.endpoint + model + "/form"
    def keys = Config.endpoint + model + "/keys"
    def count = Config.endpoint + model + "/count"
    def get(i:Int) = Config.endpoint + model + "/" + i
    def update(i:Int) = Config.endpoint + model + "/" + i
    def insert = Config.endpoint + model
    def firsts = Config.endpoint + model
  }

  def list(jsonQuery: JSONQuery) = HttpClient.postJson[JSONResponse](Paths.list,jsonQuery.asJson.noSpaces)
  def schema = HttpClient.get[JSONSchema](Paths.schema)
  def form = HttpClient.get[Vector[JSONField]](Paths.form)
  def keys = HttpClient.get[Vector[String]](Paths.keys)
  def count = HttpClient.get[JSONCount](Paths.count)
  def get(i:Int) = HttpClient.get[Map[String,String]](Paths.get(i))
  def update(i:Int,data:String) = HttpClient.putJson[String](Paths.update(i),data)
  def insert(data:String) = HttpClient.postJson[String](Paths.insert,data)
  def firsts = HttpClient.get[JSONResponse](Paths.firsts)

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

  def models() = HttpClient.get[Vector[String]](Config.endpoint + "models")

}
