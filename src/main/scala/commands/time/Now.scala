package org.maple
package commands.time

import commands.MyCommand
import services.TimezonesService
import utils.DateTimeUtils
import utils.IterableUtils._
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.requests.Request
import ackcord.syntax.TextChannelSyntax

class Now extends MyCommand {
  override def aliases: Seq[String] = Seq("now")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val timezonesService = new TimezonesService
    val timesNow = arguments.map(arg => Markdown.bold(arg.toUpperCase + ": ") + timezonesService.getTime(arg).map(DateTimeUtils.asString).getOrElse("Could not retrieve data"))
    val output = timesNow.joinLines
    msg.textChannel.sendMessage(output)
  }
}
