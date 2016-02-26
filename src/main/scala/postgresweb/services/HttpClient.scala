package postgresweb.services

import io.circe._
import io.circe.parser._
import org.scalajs.dom.ext.Ajax
import postgresweb.configs.Config

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by andreaminetti on 23/02/16.
  */
object HttpClient {

  def postJson[T](url:String, data:String)(implicit d:Decoder[T]):Future[T] =
    Ajax.post(url, data,headers = Map(Config.auth, "Content-Type" -> "application/json")).map(xhr => decode[T](xhr.responseText).toOption.get)

  def putJson[T](url:String, data:String)(implicit d:Decoder[T]):Future[T] =
    Ajax.put(url, data,headers = Map(Config.auth, "Content-Type" -> "application/json")).map(xhr => decode[T](xhr.responseText).toOption.get)

  def get[T](url:String)(implicit d:Decoder[T]):Future[T] = Ajax.get(url,headers = Map(Config.auth)).map{xhr =>
    println(xhr.responseText)
    decode[T](xhr.responseText).toOption.get
  }



}
