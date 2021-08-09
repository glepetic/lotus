package org.maple
package commands.storymaple

import commands.MyCommand
import config.BotEnvironment
import mappers.EmbedMapper
import services.RankingsService
import utils.IterableUtils._
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, DeleteMessage, Request}

class Rank extends MyCommand {
  override def aliases: Seq[String] = Seq("r","rank")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val rankingsService = new RankingsService()

    val parsedIgn = arguments.join.trim
    val player = rankingsService.getPlayer(parsedIgn)

    val embedMapper = new EmbedMapper

    val embed = player.map(p => embedMapper.toEmbed(p, msg.message.content, msg.user))
      .getOrElse(embedMapper.toErrorEmbed(s"Could not retrieve data for ${Markdown.bold(parsedIgn)}", msg.message.content, msg.user))

    BotEnvironment.client.foreach(client => client.requestsHelper.run(DeleteMessage(msg.textChannel.id, msg.message.id))(msg.cache))
    CreateMessage(msg.textChannel.id, CreateMessageData(embeds = Seq(embed)))
  }
}
