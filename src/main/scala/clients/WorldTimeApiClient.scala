package org.maple
package clients

import config.{BotEnvironment, HttpClientConfig}

import scalaj.http.Http
import spray.json._

class WorldTimeApiClient {

  val host: String = HttpClientConfig.worldTimeApiHost
  val timezones: String = HttpClientConfig.worldTimeApiTimezonesUrl

  def getTimezones: JsValue = {
    Http(host + timezones)
      .asString
      .body
      .parseJson
  }

  def getTime(timezone: String): JsValue = {
    Http(host + timezones + "/" + timezone)
      .asString
      .body
      .parseJson
  }

}
