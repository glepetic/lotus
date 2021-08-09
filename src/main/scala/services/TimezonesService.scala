package org.maple
package services

import clients.WorldTimeApiClient
import parsers.WorldTimeApiParser

import spray.json.{JsArray, JsString}

import java.time.ZonedDateTime

class TimezonesService {

  val client = new WorldTimeApiClient()
  val parser = new WorldTimeApiParser()

  def getTimezones(filter: String): Option[Vector[String]] = client
    .getTimezones
    .map(_.asInstanceOf[JsArray].elements
      .map(t => t.asInstanceOf[JsString].value)
      .filter(t => t.toLowerCase contains filter.toLowerCase)
      .map(t => this.parser.parseValue(t).getOrElse(t)))


  def getTime(timezone: String): Option[ZonedDateTime] = this.client
    .getTime(this.parser.parseKey(timezone).getOrElse(timezone))
    .map(this.parser.parse)

}

object TimezonesService {
  private val instance: TimezonesService = new TimezonesService()

  def getInstance: TimezonesService = instance
}


