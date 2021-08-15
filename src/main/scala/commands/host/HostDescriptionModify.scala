package org.maple
package commands.host

import commands.MyCommand
import config.BotEnvironment
import services.EventHostsService
import utils.IterableUtils._

import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, TextChannelId, User}
import ackcord.requests.{DeleteMessage, EditMessage, EditMessageData, Request}
import ackcord.util.JsonOption
import org.maple.model.HostedEvent
import org.mongodb.scala.Observable

class HostDescriptionModify extends MyCommand {
  override def aliases: Seq[String] = Seq("hdm", "hostDmodify")
  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val newDescription: String = arguments.joinWords.trim
    val hostsService: EventHostsService = EventHostsService.getInstance

    val hostedEventOpt: Observable[HostedEvent] = hostsService
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(!_.finalised)
      .map(he => he.withDescription(newDescription))

    hostedEventOpt.foreach(he => {
      hostsService.replace(he)
      BotEnvironment.client.foreach(client => client.requestsHelper.run(EditMessage(TextChannelId(he.channelId), MessageId(he.messageId), EditMessageData(JsonOption.fromOptionWithNull(Option(he.asString)))))(msg.cache))
    })

    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
