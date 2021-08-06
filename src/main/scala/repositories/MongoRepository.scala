package org.maple
package repositories

import config.DBConfig

import org.bson.Document
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}

trait MongoRepository {

  protected val client: MongoClient = MongoClient(DBConfig.mongoUri)

  protected def database: MongoDatabase = client.getDatabase(this.databaseName)
  protected def collection: MongoCollection[Document] = database.getCollection(this.collectionName)

  protected def databaseName: String
  protected def collectionName: String

}
