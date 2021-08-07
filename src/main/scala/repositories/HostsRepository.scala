package org.maple
package repositories

import model.maplestory.BossRun

import org.bson.Document
import org.mongodb.scala.SingleObservable
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Sorts.descending


class HostsRepository extends MongoRepository {

  override protected def databaseName: String = "default"
  override protected def collectionName: String = "hosts"

  var bossRuns: List[BossRun] = Nil

  def mongoFind(messageId: String): SingleObservable[Document] = this.collection
    .find(equal("messageId", messageId))
    .first
  def mongoFindLatest(hostId: String, channelId: String): SingleObservable[Document] = this.collection
    .find(and(equal("hostId", hostId), equal("channelId", channelId)))
    .sort(descending("timestamp"))
    .first
  def mongoInsert(d: Document): Unit = this.collection.insertOne(d).foreach(println)
  def mongoReplace(d: Document): Unit = this.collection.replaceOne(equal("id", d.getString("id")), d).foreach(println)


  def insert(br: BossRun): Unit = this.bossRuns = this.bossRuns ++ List(br)
  def findLatest(hostId: String, channelId: String): Option[BossRun] =
    this.bossRuns.sortBy(_.timestamp).findLast(br => (br.hostId equalsIgnoreCase hostId) && (br.channelId equalsIgnoreCase channelId))
  def find(messageId: String): Option[BossRun] = this.bossRuns.find(br => br.messageId equalsIgnoreCase messageId)
  def update(br: BossRun): Unit = this.bossRuns = this.bossRuns.filterNot(bossRun => bossRun.id equalsIgnoreCase br.id) ++ List(br)

}

object HostsRepository {
  private val instance = new HostsRepository
  def getInstance: HostsRepository = instance
}
