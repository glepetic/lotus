package org.maple
package listeners

import commands.MyCommand
import commands.basic.{Help, Meme}
import commands.host._
import commands.storymaple._
import commands.time.{Now, ServerTime, Timezones}
import config.BotEnvironment
import utils.DateTimeUtils

import ackcord.Requests
import ackcord.commands.MessageParser.RemainingAsString
import ackcord.commands._

class CommandListener(requests: Requests) extends CommandController(requests) {

  // basic commands

  private val help: MyCommand = new Help
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
  private val rissadk: MyCommand = new RissaDK
  private val sc: MyCommand = new Lilynouch
  // host commands
  private val host: MyCommand = new Host
  private val hostAdd: MyCommand = new HostAdd
  private val hostKick: MyCommand = new HostKick
  private val hostDescriptionModify: MyCommand = new HostDescriptionModify
  private val hostFinalise: MyCommand = new HostFinalise
  private val hostRepeat: MyCommand = new HostRepeat
  private val hostMention: MyCommand = new HostMention
  private val hostPromote: MyCommand = new HostPromote
  private val hostQuit: MyCommand = new HostQuit

  val enabledNamedCommands: List[NamedCommand[_]] = this.getEnabledNamedCommands

  private def getEnabledNamedCommands: List[NamedCommand[_]] = {
    val basicEnabledCommands = List(help, meme)
    val timeEnabledCommands = List(now, timezones, servertime)
    val hostCommands = List(host, hostAdd, hostKick, hostDescriptionModify, hostFinalise, hostRepeat, hostMention, hostPromote, hostQuit)
    val storymapleEnabledCommands = List(rank, rankNumber, guild, whodrops, whatdropsfrom, bossroster, rissadk, sc)
    val allEnabledCommands = basicEnabledCommands ++ timeEnabledCommands ++ hostCommands ++ storymapleEnabledCommands
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
