package org.maple
package commands.storymaple

import builders.EmbedBuilder
import commands.MyCommand
import config.BotEnvironment
import services.MobDropsService
import utils.IterableUtils._
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, DeleteMessage, Request}

class WhatDropsFrom extends MyCommand {
  override def aliases: Seq[String] = Seq("wdf","whatdropsfrom")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val dropsService = new MobDropsService()

    val parsedMobName = arguments.joinWords.trim
    val drops = Option(dropsService.getDrops(parsedMobName)).filter(_.nonEmpty)

    val embedBuilder = EmbedBuilder
      .builder
      .authorRequestedBy(msg.user)
      .defaultFooter

    val embed = drops.map(d => embedBuilder
      .defaultColor
      .defaultThumbnail
      .title(parsedMobName.toUpperCase)
      .description(d.map(_.name).joinLines)
      .build
    ).getOrElse(embedBuilder.defaultErrorColor.defaultErrorThumbnail.title("Item Information Error").description(s"Could not retrieve data for ${Markdown.bold(parsedMobName)}").build)

    BotEnvironment.client.foreach(client => client.requestsHelper.run(DeleteMessage(msg.textChannel.id, msg.message.id))(msg.cache))
    CreateMessage(msg.textChannel.id, CreateMessageData(embed = Option(embed)))
  }
}
