package org.maple
package parsers

import utils.JsonUtils
import utils.OptionUtils._

import spray.json.{JsString, JsValue}

import java.time.ZonedDateTime

class WorldTimeApiParser {

  val timezonesMap: JsValue = JsonUtils.readJson("/timezones.json")

  def parseKey(timezone: String): Option[String] = this.timezonesMap.asJsObject.fields.get(timezone.toUpperCase).map(t => t.asInstanceOf[JsString].value)
  def parseValue(timezone: String): Option[String] = this.timezonesMap.asJsObject.fields.keys
    .find(k => this.timezonesMap.asJsObject.fields.get(k).exists(v => v.asInstanceOf[JsString].value equals timezone))

  def parse(timezoneJson: JsValue): ZonedDateTime = {
    val timeNow: String = timezoneJson.asJsObject.fields
      .get("datetime")
      .map(d => d.asInstanceOf[JsString].value)
      .orThrow
    ZonedDateTime.parse(timeNow);
  }

}
