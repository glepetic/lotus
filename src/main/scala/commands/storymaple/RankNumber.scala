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
import org.maple.model.maplestory.Player

class RankNumber extends MyCommand {
  override def aliases: Seq[String] = Seq("rn","ranknumber")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val rankingsService = new RankingsService

    val parsedArgument = arguments.join.trim

    val playerOpt: Option[Player] = Option(parsedArgument)
      .filter(str => str forall Character.isDigit)
      .map(_.toLong)
      .flatMap(rankingsService.getPlayer)

    val embedMapper = new EmbedMapper

    val embed = playerOpt.map(p => embedMapper.toEmbed(p, msg.message.content, msg.user))
      .getOrElse(embedMapper.toErrorEmbed(s"Could not retrieve data for ${Markdown.bold(parsedArgument)}", msg.message.content, msg.user))

    BotEnvironment.client.foreach(client => client.requestsHelper.run(DeleteMessage(msg.textChannel.id, msg.message.id))(msg.cache))
    CreateMessage(msg.textChannel.id, CreateMessageData(embeds = Seq(embed)))
  }
}
