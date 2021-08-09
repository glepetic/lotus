package org.maple
package clients

import config.{BotEnvironment, HttpClientConfig}

import scalaj.http.{Http, HttpRequest}
import spray.json._

class WorldTimeApiClient extends MyClient {

  override protected def host: String = HttpClientConfig.worldTimeApiHost
  override protected def timeout: Int = HttpClientConfig.worldTimeApiTimeout

  val timezones: String = HttpClientConfig.worldTimeApiTimezonesUrl

  def getTimezones: Option[JsValue] = this.get(timezones)
  def getTime(timezone: String): Option[JsValue] = this.get(timezones + "/" + timezone)

}
