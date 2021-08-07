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

class HostKick extends MyCommand {
  override def aliases: Seq[String] = Seq("hk","hostkick")
  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val participantsToBeKicked = arguments.map(_.trim).filter(str => str forall Character.isDigit).map(_.toInt)
    val hostsService: EventHostsService = EventHostsService.getInstance
    val maybeBossRun: Observable[BossRun] = hostsService
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(br => !br.finalised)
      .map(br => BossRun(br.messageId, br.timestamp, br.hostId, br.channelId, br.description, br.id, br.mentions.zipWithIndex.filterNot{case (_,index) => participantsToBeKicked contains index+1}.map(_._1)))
    maybeBossRun.foreach(br => {
      hostsService.replace(br)
      BotEnvironment.client.foreach(client => client.requestsHelper.run(EditMessage(TextChannelId(br.channelId), MessageId(br.messageId), EditMessageData(JsonOption.fromOptionWithNull(Option(br.asString)))))(msg.cache))
    })
    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
