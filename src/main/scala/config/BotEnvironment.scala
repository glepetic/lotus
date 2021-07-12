package org.maple
package config

import utils.OptionUtils._

import ackcord.DiscordClient
import ackcord.data.User
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File

object BotEnvironment {

  val resourcesPath: String = System.getProperty("user.dir") + "/src/main/resources"

  private val config: Config = ConfigFactory.parseFile(new File(resourcesPath + "/" + "application.properties"))
  def getStringProperty(name: String): Option[String] = Option(config.getString(name))
  def getIntProperty(name: String): Option[Int] = Option(config.getInt(name))
  def getLongProperty(name: String): Option[Long] = Option(config.getLong(name))

  var client: Option[DiscordClient] = None
  val publicPath: String = this.resourcesPath + "/public"
  val token: String = System.getenv("discord.bot.token")
  val prefix: String = this.getStringProperty("bot.prefix").getOrElse("~")
  val botOwnerUsername: String = this.getStringProperty("bot.owner.user").orThrow

}
