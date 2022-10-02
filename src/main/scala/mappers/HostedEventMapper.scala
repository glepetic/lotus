package org.maple
package mappers

import org.bson.Document
import org.maple.model
import org.maple.model.HostedEvent
import spray.json.{JsArray, JsBoolean, JsNumber, JsObject, JsString, JsValue, _}

import java.time.Instant
import scala.collection.immutable.ListSet

class HostedEventMapper {

  def to(d: Document): HostedEvent = model.HostedEvent(
    d.getString("messageId"),
    Instant.ofEpochMilli(d.getLong("timestamp")),
    d.getString("hostId"),
    d.getString("channelId"),
    d.getString("threadId"),
    d.getString("description"),
    d.getString("id"),
    this.getAndMapListSetFromDocument(d, "cohosts"),
    this.getAndMapListSetFromDocument(d, "participants"),
    d.getBoolean("finalised")
  )

  def to(he: HostedEvent): Document = Document.parse(this.toJsValue(he).prettyPrint)

  private def toJsValue(he: HostedEvent): JsValue = JsObject(
    "messageId" -> JsString(he.messageId),
    "timestamp" -> JsNumber(he.timestamp.toEpochMilli),
    "hostId" -> JsString(he.hostId),
    "channelId" -> JsString(he.channelId),
    "threadId" -> JsString(he.threadId),
    "description" -> JsString(he.description),
    "id" -> JsString(he.id),
    "cohosts" -> JsArray.apply(he.cohosts.map(JsString.apply).toVector),
    "participants" -> JsArray.apply(he.participants.map(JsString.apply).toVector),
    "finalised" -> JsBoolean(he.finalised)
  )

  private def getAndMapListSetFromDocument(d: Document, key: String): ListSet[String] = ListSet.from(this.getAndMapCollectionFromDocument(d, key))
  private def getAndMapCollectionFromDocument(d: Document, key: String): Iterable[String] = d.toJson.parseJson.asJsObject.fields.getOrElse(key, JsArray.empty).asInstanceOf[JsArray].elements.map(_.asInstanceOf[JsString].value)

}
