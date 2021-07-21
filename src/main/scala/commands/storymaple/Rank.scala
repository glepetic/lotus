package org.maple
package commands.storymaple

import builders.EmbedBuilder
import commands.MyCommand
import services.RankingsService
import utils.IterableUtils._
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, DeleteMessage, Request}
import org.maple.config.BotEnvironment

class Rank extends MyCommand {
  override def aliases: Seq[String] = Seq("r","rank")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val rankingsService = new RankingsService()

    val parsedIgn = arguments.join.trim
    val player = rankingsService.getPlayer(parsedIgn)

    val embedBuilder = EmbedBuilder
      .builder
      .authorRequestedBy(msg.user)
      .defaultFooter

    val embed = player.map(p => embedBuilder
      .defaultColor
      .defaultThumbnail
      .title("Player Information")
      .description(p.characters.map(c => s"${Markdown.bold(c.ign)} - Lvl. ${c.level} - ${Markdown.bold(c.job.name())}${Option(c.guild).map(g => " - " + g).getOrElse("")}").joinLines)
      .withField("Rank", p.rank.toString)
      .withField("Link Levels", p.linkLevels.toString)
      .withField("Nirvana", p.nirvana.toString)
      .build
    ).getOrElse(embedBuilder.defaultErrorColor.defaultErrorThumbnail.title("Player Information Error").description(s"Could not retrieve data for ${Markdown.bold(parsedIgn)}").build)

    BotEnvironment.client.foreach(client => client.requestsHelper.run(DeleteMessage(msg.textChannel.id, msg.message.id))(msg.cache))
    CreateMessage(msg.textChannel.id, CreateMessageData(embed = Option(embed)))
  }
}
