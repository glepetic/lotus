package org.maple
package commands.storymaple.bossing

import commands.MyCommand
import config.BotEnvironment
import model.maplestory.BossRun
import services.EventHostsService

import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, TextChannelId, User}
import ackcord.requests.{DeleteMessage, EditMessage, EditMessageData, Request}
import ackcord.util.JsonOption
import org.mongodb.scala.Observable

class HostAdd extends MyCommand {
  override def aliases: Seq[String] = Seq("ha","hostadd")
  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val participants = arguments.map(_.trim)
    val hostsService: EventHostsService = EventHostsService.getInstance
    val maybeBossRun: Observable[BossRun] = hostsService
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(br => !br.finalised)
      .map(br => BossRun(br.messageId, br.timestamp, br.hostId, br.channelId, br.description, br.id, br.mentions ++ participants))
    maybeBossRun.foreach(br => {
      hostsService.replace(br)
      BotEnvironment.client.foreach(client => client.requestsHelper.run(EditMessage(TextChannelId(br.channelId), MessageId(br.messageId), EditMessageData(JsonOption.fromOptionWithNull(Option(br.asString)))))(msg.cache))
    })
    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
