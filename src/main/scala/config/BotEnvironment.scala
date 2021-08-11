package org.maple
package config

import utils.OptionUtils._

import ackcord.DiscordClient
import ackcord.data.User
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File

object BotEnvironment {

  System.setProperty("jdk.tls.client.protocols", "TLSv1.2")

  val resourcesPath: String = System.getProperty("user.dir") + "/src/main/resources"

  private val config: Config = ConfigFactory.parseFile(new File(resourcesPath + "/" + "application.properties"))
  def getStringProperty(name: String): Option[String] = Option(config.getString(name))
  def getIntProperty(name: String): Option[Int] = Option(config.getInt(name))
  def getLongProperty(name: String): Option[Long] = Option(config.getLong(name))

  var client: Option[DiscordClient] = None
  val publicPath: String = this.resourcesPath + "/public"
  val token: String = sys.env.get("discord.bot.token").orThrow
  val prefix: String = sys.env.getOrElse("discord.bot.prefix", ".")
  val botOwnerUsername: String = this.getStringProperty("bot.owner.user").orThrow
  val botOwnerId: String = this.getStringProperty("bot.owner.id").orThrow

}
