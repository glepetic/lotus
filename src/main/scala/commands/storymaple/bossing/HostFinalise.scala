package org.maple
package commands.storymaple.bossing

import commands.MyCommand
import config.BotEnvironment
import model.maplestory.BossRun
import services.EventHostsService

import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, TextChannelId, User}
import ackcord.requests._
import ackcord.util.JsonOption
import org.mongodb.scala.Observable

class HostFinalise extends MyCommand{
  override def aliases: Seq[String] = Seq("hf", "hostfinalise")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val hostsService: EventHostsService = EventHostsService.getInstance

    val maybeBossRun: Observable[BossRun] = hostsService
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(br => !br.finalised)
      .map(_.finalise)

    maybeBossRun.foreach(br => {
      val channel = TextChannelId(br.channelId)
      val message = MessageId(br.messageId)
      hostsService.replace(br)
      BotEnvironment.client.foreach(client => {
        client.requestsHelper.run(EditMessage(channel, message, EditMessageData(JsonOption.fromOptionWithNull(Option(br.asString)))))(msg.cache)
        client.requestsHelper.run(DeleteAllReactions(channel, message))(msg.cache)
      })
    })

    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
