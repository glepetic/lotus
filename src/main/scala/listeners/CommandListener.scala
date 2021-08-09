package org.maple
package listeners

import commands.MyCommand
import commands.basic.{Hello, Meme, Ping, React}
import commands.storymaple.{GuildCharacters, Rank, RankNumber, WhatDropsFrom, WhoDrops}
import commands.time.{Now, ServerTime, Timezones}
import config.BotEnvironment
import utils.DateTimeUtils

import ackcord.Requests
import ackcord.commands.MessageParser.RemainingAsString
import ackcord.commands._
import org.maple.commands.storymaple.bossing.{BossRoster, Host, HostAdd, HostDescriptionModify, HostFinalise, HostKick, HostRepeat}

class CommandListener(requests: Requests) extends CommandController(requests) {

  // basic commands
  private val ping: MyCommand = new Ping
  private val hello: MyCommand = new Hello
  private val react: MyCommand = new React
  private val meme: MyCommand = new Meme
  // time commands
  private val now: MyCommand = new Now
  private val timezones: MyCommand = new Timezones
  private val servertime: MyCommand = new ServerTime
  // storymaple commands
  private val rank: MyCommand = new Rank
  private val rankNumber: MyCommand = new RankNumber
  private val guild: MyCommand = new GuildCharacters
  private val whodrops: MyCommand = new WhoDrops
  private val whatdropsfrom: MyCommand = new WhatDropsFrom
  private val bossroster: MyCommand = new BossRoster
  private val host: MyCommand = new Host
  private val hostAdd: MyCommand = new HostAdd
  private val hostKick: MyCommand = new HostKick
  private val hostDescriptionModify: MyCommand = new HostDescriptionModify
  private val hostFinalise: MyCommand = new HostFinalise
  private val hostRepeat: MyCommand = new HostRepeat

  val enabledNamedCommands: List[NamedCommand[_]] = this.getEnabledNamedCommands

  private def getEnabledNamedCommands: List[NamedCommand[_]] = {
    val basicEnabledCommands = List(ping, hello, meme)
    val timeEnabledCommands = List(now, timezones, servertime)
    val storymapleEnabledCommands = List(rank, rankNumber, guild, whodrops, whatdropsfrom, bossroster, host, hostAdd, hostKick, hostDescriptionModify, hostFinalise, hostRepeat)
    val allEnabledCommands = basicEnabledCommands ++ timeEnabledCommands ++ storymapleEnabledCommands
    allEnabledCommands.map(c => this.toNamedCommand(c))
  }

  private def toNamedCommand(command: MyCommand): NamedCommand[_] = {
    Command
      .named(Seq(BotEnvironment.prefix), command.aliases)
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
