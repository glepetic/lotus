package org.maple
package mappers

import model.maplestory.BossRun

import org.bson.Document
import spray.json.{JsArray, JsBoolean, JsNumber, JsObject, JsString, JsValue, _}

import java.time.Instant
import scala.collection.immutable.ListSet

class BossRunMapper {

  def to(d: Document): BossRun = BossRun(
    d.getString("messageId"),
    Instant.ofEpochMilli(d.getLong("timestamp")),
    d.getString("hostId"),
    d.getString("channelId"),
    d.getString("description"),
    d.getString("id"),
    this.getAndMapCollectionFromDocument(d, "cohosts").toList,
    ListSet.from(this.getAndMapCollectionFromDocument(d, "participants")),
    d.getBoolean("finalised")
  )

  def to(br: BossRun): Document = Document.parse(this.toJsValue(br).prettyPrint)

  private def toJsValue(br: BossRun): JsValue = JsObject(
    "messageId" -> JsString(br.messageId),
    "timestamp" -> JsNumber(br.timestamp.toEpochMilli),
    "hostId" -> JsString(br.hostId),
    "channelId" -> JsString(br.channelId),
    "description" -> JsString(br.description),
    "id" -> JsString(br.id),
    "cohosts" -> JsArray.apply(br.cohosts.map(JsString.apply).toVector),
    "participants" -> JsArray.apply(br.participants.map(JsString.apply).toVector),
    "finalised" -> JsBoolean(br.finalised)
  )

  private def getAndMapCollectionFromDocument(d: Document, key: String): Iterable[String] = d.toJson.parseJson.asJsObject.fields.getOrElse(key, JsArray.empty).asInstanceOf[JsArray].elements.map(_.asInstanceOf[JsString].value)

}
