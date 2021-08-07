package org.maple
package repositories

import org.bson.Document
import org.mongodb.scala.SingleObservable
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Sorts.descending


class HostsRepository extends MongoRepository {

  override protected def databaseName: String = "default"
  override protected def collectionName: String = "hosts"

  def find(messageId: String): SingleObservable[Document] = this.collection
    .find(equal("messageId", messageId))
    .first
  def findLatest(hostId: String, channelId: String): SingleObservable[Document] = this.collection
    .find(and(equal("hostId", hostId), equal("channelId", channelId)))
    .sort(descending("timestamp"))
    .first
  def insert(d: Document): Unit = this.collection.insertOne(d).foreach(println)
  def replace(d: Document): Unit = this.collection.replaceOne(equal("id", d.getString("id")), d).foreach(println)

}

object HostsRepository {
  private val instance = new HostsRepository
  def getInstance: HostsRepository = instance
}
