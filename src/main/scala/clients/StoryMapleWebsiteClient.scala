package org.maple
package clients

import config.HttpClientConfig

import scalaj.http.{Http, HttpRequest}
import spray.json._

class StoryMapleWebsiteClient {

  private val host: String = HttpClientConfig.storymapleWebsiteHost
  private val rankings: String = HttpClientConfig.rankingsUrl
  private val drops: String = HttpClientConfig.dropsUrl

  def getRankings: Option[JsValue] = this.get(this.rankings)
  def getMobDrops: Option[JsValue] = this.get(this.drops)

  private def get(resource: String): Option[JsValue] = {
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

  private def buildRequest(url: String): HttpRequest = Http(host + url)
    .timeout(HttpClientConfig.storyMapleTimeout, HttpClientConfig.storyMapleTimeout)

}
