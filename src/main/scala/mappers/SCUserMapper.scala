package org.maple
package mappers

import model.SCUser

import org.bson.Document
import spray.json.{JsNumber, JsObject, JsString, JsValue}

import java.time.Instant

class SCUserMapper {

  def to(d: Document): SCUser = SCUser(
    d.getString("userId"),
    Instant.ofEpochMilli(d.getLong("lastRoll")),
    d.getInteger("scCount"),
    d.getInteger("donutCount"),
    d.getInteger("scrollCount"),
    d.getString("id")
  )

  def to(usr: SCUser): Document = Document.parse(this.toJsValue(usr).prettyPrint)

  private def toJsValue(scUser: SCUser): JsValue = JsObject(
    "userId" -> JsString(scUser.userId),
    "lastRoll" -> JsNumber(scUser.lastRoll.toEpochMilli),
    "scCount" -> JsNumber(scUser.scCount),
    "donutCount" -> JsNumber(scUser.donutCount),
    "scrollCount" -> JsNumber(scUser.scrollCount),
    "id" -> JsString(scUser.id),
  )

}
