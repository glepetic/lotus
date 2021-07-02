package org.maple
package services

import clients.WorldTimeApiClient
import parsers.WorldTimeApiParser

import spray.json.{JsArray, JsString}

import java.time.ZonedDateTime

class TimezonesService {

  val client = new WorldTimeApiClient()
  val parser = new WorldTimeApiParser()

  def getTimezones(filter: String): Vector[String] = client
    .getTimezones.asInstanceOf[JsArray].elements
    .map(t => t.asInstanceOf[JsString].value)
    .filter(t => t.toLowerCase contains filter.toLowerCase)
    .map(t => this.parser.parseValue(t).getOrElse(t))

  def getTime(timezone: String): ZonedDateTime = this.parser.parse(this.client.getTime(this.parser.parseKey(timezone).getOrElse(timezone)))

}

object TimezonesService {
  private val instance: TimezonesService = new TimezonesService()
  def getInstance: TimezonesService = instance
}


