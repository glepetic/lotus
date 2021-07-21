package org.maple
package commands.storymaple

import builders.EmbedBuilder
import commands.MyCommand
import config.BotEnvironment
import services.{MobDropsService, RankingsService}
import utils.IterableUtils._
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, DeleteMessage, Request}

class WhoDrops extends MyCommand {
  override def aliases: Seq[String] = Seq("wd","whodrops")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val dropsService = new MobDropsService()

    val parsedItemName = arguments.joinWords.trim
    val item = dropsService.getDrop(parsedItemName)

    val embedBuilder = EmbedBuilder
      .builder
      .authorRequestedBy(msg.user)
      .defaultFooter

    val embed = item.map(i => embedBuilder
      .defaultColor
      .defaultThumbnail
      .title(i.name.toUpperCase)
      .description(i.mobs.joinLines)
      .build
    ).getOrElse(embedBuilder.defaultErrorColor.defaultErrorThumbnail.title("Item Information Error").description(s"Could not retrieve data for ${Markdown.bold(parsedItemName)}").build)

    BotEnvironment.client.foreach(client => client.requestsHelper.run(DeleteMessage(msg.textChannel.id, msg.message.id))(msg.cache))
    CreateMessage(msg.textChannel.id, CreateMessageData(embed = Option(embed)))
  }
}
