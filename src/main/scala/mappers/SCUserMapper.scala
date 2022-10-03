package org.maple
package mappers

import model.SCUser

import org.bson.Document
import spray.json.{JsNumber, JsObject, JsString, JsValue}

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SCUserMapper {

  val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE

  def to(d: Document): SCUser = SCUser(
    d.getString("userId"),
    d.getString("serverId"),
    LocalDate.parse(d.getString("lastRoll"), dateFormatter),
    d.getInteger("scCount").toLong,
    d.getInteger("donutCount").toLong,
    d.getInteger("scrollCount").toLong,
    d.getString("id")
  )

  def to(usr: SCUser): Document = Document.parse(this.toJsValue(usr).prettyPrint)

  private def toJsValue(scUser: SCUser): JsValue = JsObject(
    "userId" -> JsString(scUser.userId),
    "serverId" -> JsString(scUser.serverId),
    "lastRoll" -> JsString(scUser.lastRoll.format(dateFormatter)),
    "scCount" -> JsNumber(scUser.scCount),
    "donutCount" -> JsNumber(scUser.donutCount),
    "scrollCount" -> JsNumber(scUser.scrollCount),
    "id" -> JsString(scUser.id),
  )

}
