package org.maple
package clients

import config.HttpClientConfig

import scalaj.http.Http
import spray.json._

class StoryMapleWebsiteClient {

  val host: String = HttpClientConfig.storymapleWebsiteHost
  val rankings: String = HttpClientConfig.rankingsUrl

  def getRankings: Option[JsValue] = {
    try
      Option(Http(host + rankings))
        .map(req => req.asString)
        .filter(res => res.is2xx)
        .map(res => res.body.parseJson)
    catch {
      case e: Exception =>
        println(e.getMessage)
        None
    }


  }

}
