package org.maple
package listeners

import commands.MyCommand
import commands.basic.{Hello, Meme, Ping, React}
import commands.storymaple.Rank
import commands.time.{Now, Timezones}
import config.BotEnvironment
import utils.DateTimeUtils

import ackcord.Requests
import ackcord.commands.MessageParser.RemainingAsString
import ackcord.commands._

class CommandListener(requests: Requests) extends CommandController(requests) {

  // basic commands
  private val ping: MyCommand = new Ping
  private val hello: MyCommand = new Hello
  private val react: MyCommand = new React
  private val meme: MyCommand = new Meme
  // time commands
  private val now: MyCommand = new Now
  private val timezones: MyCommand = new Timezones
  // storymaple commands
  private val rank: MyCommand = new Rank

  val enabledNamedCommands: List[NamedCommand[_]] = this.getEnabledNamedCommands;

  private def getEnabledNamedCommands: List[NamedCommand[_]] = {
    val basicEnabledCommands = List(ping, hello, meme)
    val timeEnabledCommands = List(now, timezones)
    val storymapleEnabledCommands = List(rank)
    val allEnabledCommands = basicEnabledCommands ++ timeEnabledCommands ++ storymapleEnabledCommands
    allEnabledCommands.map(c => this.toNamedCommand(c))
  }

  private def toNamedCommand(command: MyCommand): NamedCommand[_] = {
    Command
      .named(Seq(BotEnvironment.prefix), Seq(command.name()))
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
