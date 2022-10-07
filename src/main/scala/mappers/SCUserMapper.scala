package org.maple
package mappers

import model.SCUser

import org.bson.Document
import spray.json.{JsNull, JsNumber, JsObject, JsString, JsValue}

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SCUserMapper {

  val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE

  def to(d: Document): SCUser = SCUser(
    d.getString("userId"),
    d.getString("serverId"),
    Option(d.getString("lastRoll"))
      .map(lR => LocalDate.parse(lR, dateFormatter))
      .orNull,
    d.getInteger("scCount").toLong,
    d.getInteger("donutCount").toLong,
    d.getInteger("scrollCount").toLong,
    d.getString("id")
  )

  def to(usr: SCUser): Document = Document.parse(this.toJsValue(usr).prettyPrint)

  private def toJsValue(scUser: SCUser): JsValue = JsObject(
    "userId" -> JsString(scUser.userId),
    "serverId" -> JsString(scUser.serverId),
    "lastRoll" -> Option(scUser.lastRoll)
      .map(lR => JsString(lR.format(dateFormatter)))
      .getOrElse(JsNull),
    "scCount" -> JsNumber(scUser.scCount),
    "donutCount" -> JsNumber(scUser.donutCount),
    "scrollCount" -> JsNumber(scUser.scrollCount),
    "id" -> JsString(scUser.id),
  )

}
