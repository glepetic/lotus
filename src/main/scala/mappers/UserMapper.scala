package org.maple
package mappers

import model.User

import org.bson.Document
import spray.json.{JsNull, JsNumber, JsObject, JsString, JsValue}

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserMapper {

  val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE

  def to(d: Document): User = User(
    d.getString("userId"),
    d.getString("serverId"),
    Option(d.getString("lastRoll"))
      .map(lR => LocalDate.parse(lR, dateFormatter))
      .orNull,
    d.getInteger("scCount").toLong,
    d.getInteger("donutCount").toLong,
    d.getInteger("scrollCount").toLong,
    d.getInteger("boomerStampCount").toLong,
    d.getString("id")
  )

  def to(usr: User): Document = Document.parse(this.toJsValue(usr).prettyPrint)

  private def toJsValue(user: User): JsValue = JsObject(
    "userId" -> JsString(user.userId),
    "serverId" -> JsString(user.serverId),
    "lastRoll" -> Option(user.lastRoll)
      .map(lR => JsString(lR.format(dateFormatter)))
      .getOrElse(JsNull),
    "scCount" -> JsNumber(user.scCount),
    "donutCount" -> JsNumber(user.donutCount),
    "scrollCount" -> JsNumber(user.scrollCount),
    "boomerStampCount" -> JsNumber(user.boomerStampCount),
    "id" -> JsString(user.id),
  )

}
