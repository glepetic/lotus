package org.maple
package mappers

import model.SCUser

import org.bson.Document
import spray.json.{JsNumber, JsObject, JsString, JsValue}

import java.time.Instant

class SCUserMapper {

  def to(d: Document): SCUser = SCUser(
    d.getString("userId"),
    d.getString("severId"),
    Instant.ofEpochMilli(d.getLong("lastRoll")),
    d.getInteger("scCount"),
    d.getInteger("donutCount"),
    d.getInteger("scrollCount"),
    d.getString("id")
  )

  def to(he: SCUser): Document = Document.parse(this.toJsValue(he).prettyPrint)

  private def toJsValue(scUser: SCUser): JsValue = JsObject(
    "userId" -> JsString(scUser.userId),
    "serverId" -> JsString(scUser.serverId),
    "lastRoll" -> JsNumber(scUser.lastRoll.toEpochMilli),
    "scCount" -> JsNumber(scUser.scCount),
    "donutCount" -> JsNumber(scUser.donutCount),
    "scrollCount" -> JsNumber(scUser.scrollCount),
    "id" -> JsString(scUser.id),
  )

}
