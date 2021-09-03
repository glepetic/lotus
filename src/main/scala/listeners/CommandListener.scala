package org.maple
package listeners

import commands.MyCommand
import config.BotEnvironment
import services.CommandsService
import utils.DateTimeUtils

import ackcord.Requests
import ackcord.commands.MessageParser.RemainingAsString
import ackcord.commands._

class CommandListener(requests: Requests) extends CommandController(requests) {

  val enabledNamedCommands: List[NamedCommand[_]] = this.getEnabledNamedCommands

  private def getEnabledNamedCommands: List[NamedCommand[_]] = CommandsService.getInstance.enabledCommands.map(c => this.toNamedCommand(c))

  private def toNamedCommand(command: MyCommand): NamedCommand[_] = {
    Command
      .named(Seq(BotEnvironment.prefix.toString), command.aliases)
      .parsing(MessageParser[RemainingAsString])
      .withRequest(m => {
        this.logCommandCall(m)
        val arguments = m.message.content.split(" ").toList.tail
        command.execute(m, arguments)
      })
  }

  private def logCommandCall(m: UserCommandMessage[RemainingAsString]): Unit = {
    val timestamp = DateTimeUtils.asString(DateTimeUtils.nowAtZone("AGT"))
    val audit = m.message.authorUser(m.cache)
      .map(u => s"${u.username}#${u.discriminator}" +
        m.message.guild(m.cache)
          .map(g => s" -> ${g.name}")
          .getOrElse(""))
      .getOrElse("")
    val prefix = s"[${timestamp} - ${audit}]"
    println(s"${prefix}: ${m.message.content}")
  }

}
