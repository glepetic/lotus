package org.maple
package config

import utils.OptionUtils._

object MessageProperties {

  val maxMessageLength: Int = BotEnvironment.getIntProperty("bot.message.length.max").orThrow
  val helpMessageThumbnail: Option[String] = BotEnvironment.getStringProperty("bot.message.embed.thumbnail.help")
  val helpWiki: Option[String] = BotEnvironment.getStringProperty("bot.message.embed.url.help.wiki")
  val defaultThumbnail: Option[String] = BotEnvironment.getStringProperty("bot.message.embed.thumbnail.default")
  val defaultErrorThumbnail: Option[String] = BotEnvironment.getStringProperty("bot.error.message.embed.thumbnail.default")

}
