package org.maple
package config

object DBConfig {

  private val mongoConnectionString = "mongodb+srv://<user>:<password>@lotuscord-cluster.exq3t.mongodb.net/default?retryWrites=true&w=majority"
  private val mongoUser = "root"
  private val mongoPassword = "Iindl3T7KSwsFooN"
  val mongoUri: String = mongoConnectionString
    .replace("<user>", mongoUser)
    .replace("<password>", mongoPassword)
}
