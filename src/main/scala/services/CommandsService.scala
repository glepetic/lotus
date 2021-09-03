package org.maple
package services

import commands.MyCommand
import commands.basic.Meme
import commands.host.{Host, HostAdd, HostDescriptionModify, HostFinalise, HostKick, HostMention, HostPromote, HostQuit, HostRepeat}
import commands.storymaple.{BossRoster, GuildCharacters, Rank, RankNumber, WhatDropsFrom, WhoDrops}
import commands.time.{Now, ServerTime, Timezones}

class CommandsService {

  // basic commands
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

  def get(name: String): Option[MyCommand] = this.enabledCommands.find(command => command.aliases.contains(name))

  def enabledCommands: List[MyCommand] = {
    val basicEnabledCommands = List(meme)
    val timeEnabledCommands = List(now, timezones, servertime)
    val hostCommands = List(host, hostAdd, hostKick, hostDescriptionModify, hostFinalise, hostRepeat, hostMention, hostPromote, hostQuit)
    val storymapleEnabledCommands = List(rank, rankNumber, guild, whodrops, whatdropsfrom, bossroster)
    basicEnabledCommands ++ timeEnabledCommands ++ hostCommands ++ storymapleEnabledCommands
  }

}

object CommandsService {
  private val instance: CommandsService = new CommandsService()

  def getInstance: CommandsService = instance
}








