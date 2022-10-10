package org.maple
package commands.basic

import builders.EmbedBuilder
import commands.MyCommand
import config.MessageProperties
import utils.OptionUtils.OptionImprovements

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, Request}

class Help extends MyCommand {
  override def aliases: Seq[String] = Seq("help")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val embed = EmbedBuilder
      .builder
      .authorRequestedBy(msg.user)
      .defaultColor
      .thumbnail(MessageProperties.helpMessageThumbnail)
      .defaultFooter
      .title("Help - Click Me!")
      .url(MessageProperties.helpWiki)
      .description("Click on the link above to access the LotusBot wiki!")
      .build

    CreateMessage(msg.textChannel.id, CreateMessageData(embeds = Seq(embed)))
    CreateMessage(msg.textChannel.id, CreateMessageData("@everyone"))
  }

}
