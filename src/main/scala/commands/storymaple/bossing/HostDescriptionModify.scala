package org.maple
package commands.storymaple.bossing

import commands.MyCommand
import config.BotEnvironment
import model.maplestory.BossRun
import repositories.HostsRepository
import utils.IterableUtils._

import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, TextChannelId, User}
import ackcord.requests.{DeleteMessage, EditMessage, EditMessageData, Request}
import ackcord.util.JsonOption

class HostDescriptionModify extends MyCommand {
  override def aliases: Seq[String] = Seq("hdm", "hostDmodify")
  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val newDescription: String = arguments.joinWords.trim
    val hostsRepository: HostsRepository = HostsRepository.getInstance

    val maybeBossRun: Option[BossRun] = hostsRepository
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(br => !br.finalised)
      .map(br => BossRun(br.messageId, br.timestamp, br.hostId, br.channelId, newDescription, br.mentions))
    val maybeBossRunAsString: Option[String] = maybeBossRun.map(_.asString)

    maybeBossRun.foreach(br => {
      hostsRepository.update(br)
      BotEnvironment.client.foreach(client => client.requestsHelper.run(EditMessage(TextChannelId(br.channelId), MessageId(br.messageId), EditMessageData(JsonOption.fromOptionWithNull(maybeBossRunAsString))))(msg.cache))
    })

    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
