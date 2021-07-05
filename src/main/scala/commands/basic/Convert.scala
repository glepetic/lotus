package org.maple
package commands.basic

import commands.MyCommand
import config.BotEnvironment
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.requests.Request
import ackcord.syntax.TextChannelSyntax

class Convert extends MyCommand {
  override def aliases: Seq[String] = Seq("convert")
  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
      val content = arguments match {
        case List(originUnit, targetUnit, value) => ""
        case _ => s"Wrong command usage, please use the correct format: ${Markdown.hightlight(BotEnvironment.prefix + "convert input_unit output_unit value")}"
      }

    msg.textChannel.sendMessage(content)
  }
}
