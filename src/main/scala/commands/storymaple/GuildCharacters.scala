package org.maple
package commands.storymaple

import builders.EmbedBuilder
import commands.MyCommand
import config.BotEnvironment
import services.RankingsService
import utils.IterableUtils._
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, DeleteMessage, Request}
import org.maple.clients.MyDiscordClient

class GuildCharacters extends MyCommand {
  override def aliases: Seq[String] = Seq("g","guild")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val rankingsService = new RankingsService()

    val parsedGuildName = arguments.join.trim
    val guild = rankingsService.getGuild(parsedGuildName)

    val embedBuilder = EmbedBuilder
      .builder
      .authorRequestedBy(msg.user)
      .defaultFooter

    val embed = guild.map(g => embedBuilder
      .defaultColor
      .defaultThumbnail
      .title(g.name.toUpperCase)
      .description(g.members.map(c => c.ign).joinWords)
      .withField("Members", g.members.length.toString)
      .withField("Total Levels", g.totalLevels.toString)
      .build
    ).getOrElse(embedBuilder.defaultErrorColor.defaultErrorThumbnail.title("Guild Information Error").description(s"Could not retrieve data for ${Markdown.bold(parsedGuildName)}").build)

    MyDiscordClient.withCache(msg.cache).deleteMessage(msg.message)
    CreateMessage(msg.textChannel.id, CreateMessageData(embed = Option(embed)))
  }
}
