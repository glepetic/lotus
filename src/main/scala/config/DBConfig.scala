package org.maple
package config

object DBConfig {

  private val mongoConnectionString = "mongodb+srv://<user>:<password>@lotuscord-cluster.exq3t.mongodb.net/default?retryWrites=true&w=majority"
  private val mongoUser = sys.env.getOrElse("mongo.user", "root")
  private val mongoPassword = sys.env.getOrElse("mongo.password", "root")
  val mongoUri: String = mongoConnectionString
    .replace("<user>", mongoUser)
    .replace("<password>", mongoPassword)
  val defaultDB: String = sys.env.getOrElse("mongo.database", "test")
}
