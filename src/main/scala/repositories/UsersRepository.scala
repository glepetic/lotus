package org.maple
package repositories

import config.DBConfig

import org.bson.Document
import org.mongodb.scala.SingleObservable
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Sorts.descending

import scala.concurrent.Future


class UsersRepository extends MongoRepository {

  override protected def databaseName: String = DBConfig.defaultDB
  override protected def collectionName: String = "users"

  def findTop10ByScCount(serverId: String): Future[Seq[Document]] = this.findTopNByField(serverId, 10, "scCount")

  def findTopNByField(serverId: String, n: Int, field: String): Future[Seq[Document]] = this.collection
    .find(equal("serverId", serverId))
    .sort(descending(field))
    .limit(n)
    .toFuture()

  def find(discordUserId: String, discordServerId: String): Future[Option[Document]] = this.collection
    .find(and(equal("userId", discordUserId), equal("serverId", discordServerId)))
    .sort(descending("lastRoll"))
    .first()
    .toFutureOption()

  def insert(d: Document): Unit = this.collection
    .insertOne(d)
    .foreach(println)

  def replace(d: Document): Unit = this.collection
    .replaceOne(equal("id", d.getString("id")), d)
    .foreach(println)

}

object UsersRepository {
  private val instance = new UsersRepository
  def getInstance: UsersRepository = instance
}


