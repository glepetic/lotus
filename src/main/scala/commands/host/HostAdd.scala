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
import org.maple.utils.IterableUtils.IterableImprovements
import org.mongodb.scala.Observable

class HostAdd extends MyCommand {
  override def aliases: Seq[String] = Seq("ha","hostadd")
  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val participants = arguments.join(" ").split("\\+\\+").map(_.trim)
    val hostsService: EventHostsService = EventHostsService.getInstance
    val hostedEventOpt: Observable[HostedEvent] = hostsService
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(!_.finalised)
      .map(he => he.withParticipants(he.participants ++ participants))
    hostedEventOpt.foreach(he => {
      hostsService.replace(he)
      BotEnvironment.client.foreach(client => client.requestsHelper.run(EditMessage(TextChannelId(he.channelId), MessageId(he.messageId), EditMessageData(JsonOption.fromOptionWithNull(Option(he.asString)))))(msg.cache))
    })
    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
