package org.maple
package commands.time

import commands.MyCommand
import config.{BotEnvironment, MessageProperties}
import services.TimezonesService
import utils.IterableUtils._
import utils.discord.Markdown.codeSnippet

import ackcord.commands.UserCommandMessage
import ackcord.requests.Request
import ackcord.syntax.TextChannelSyntax

class Timezones extends MyCommand {
  override def aliases: Seq[String] = Seq("timezones")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val parsedArgs = arguments.join
    val timezonesService = new TimezonesService

    val timezones = timezonesService.getTimezones(parsedArgs)
    val timezoneString = Option(timezones.joinWords)
      .filter(t => t.nonEmpty)

    val output = timezoneString match {
      case None => "No results were found for the given filter."
      case opt => opt.map(t => codeSnippet(t)).filter(t => t.length <= MessageProperties.maxMessageLength) match {
        case None => "Too many results! Please narrow down your search."
        case Some(c) => c
      }
    }

    msg.textChannel.sendMessage(output)
  }
}
