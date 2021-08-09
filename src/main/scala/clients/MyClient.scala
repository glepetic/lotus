package org.maple
package clients

import scalaj.http.{Http, HttpRequest}
import spray.json._

trait MyClient {

  protected def host: String
  protected def timeout: Int

  protected def get(resource: String): Option[JsValue] = {
    try
      Option(this.buildRequest(resource))
        .map(req => req.asString)
        .filter(res => res.is2xx)
        .map(res => res.body.parseJson)
    catch {
      case e: Exception =>
        println(e.getMessage)
        None
    }
  }

  private def buildRequest(url: String): HttpRequest = Http(host + url).timeout(timeout, timeout)

}
