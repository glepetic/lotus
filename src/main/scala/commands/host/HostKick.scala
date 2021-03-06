package org.maple
package commands.host

import commands.MyCommand
import config.BotEnvironment
import services.EventHostsService

import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, TextChannelId, User}
import ackcord.requests.{DeleteMessage, EditMessage, EditMessageData, Request}
import ackcord.util.JsonOption
import org.maple.model.HostedEvent
import org.mongodb.scala.Observable

class HostKick extends MyCommand {
  override def aliases: Seq[String] = Seq("hk","hostkick")
  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val participantsToBeKicked = arguments.map(_.trim).filter(str => str forall Character.isDigit).map(_.toInt)
    val hostsService: EventHostsService = EventHostsService.getInstance
    val hostedEventOpt: Observable[HostedEvent] = hostsService
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(!_.finalised)
      .map(he => he.withParticipants(he.participants.zipWithIndex.filterNot{case (_,index) => participantsToBeKicked contains index+1}.map(_._1)))
    hostedEventOpt.foreach(he => {
      hostsService.replace(he)
      BotEnvironment.client.foreach(client => client.requestsHelper.run(EditMessage(TextChannelId(he.channelId), MessageId(he.messageId), EditMessageData(JsonOption.fromOptionWithNull(Option(he.asString)))))(msg.cache))
    })
    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
