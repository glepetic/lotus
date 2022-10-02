package org.maple
package commands.storymaple

import commands.MyCommand
import mappers.JobNameMapper
import services.RankingsService
import utils.OptionUtils.OptionImprovements

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, Request}

class GetCharacter extends MyCommand {
  override def aliases: Seq[String] = Seq("gc", "getcharacter")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val rankingsService = new RankingsService
    val jobMapper = new JobNameMapper

    val ign = arguments match {
      case Nil => throw new RuntimeException
      case List(_) => throw new RuntimeException
      case List(ign, job) =>
        rankingsService.getPlayer(ign)
          .map(p => p.highestLevelOfJobBranch(jobMapper.toFullName(job).orThrow))
          .map(char => char.ign)
          .orThrow
      case _ => throw new RuntimeException
    }

    CreateMessage(msg.textChannel.id, CreateMessageData(ign))
  }
}
