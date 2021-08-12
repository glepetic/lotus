package org.maple
package commands.host

import commands.MyCommand
import config.BotEnvironment
import services.EventHostsService

import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, TextChannelId, User}
import ackcord.requests._
import ackcord.util.JsonOption
import org.maple.model.HostedEvent
import org.mongodb.scala.Observable

class HostFinalise extends MyCommand{
  override def aliases: Seq[String] = Seq("hf", "hostfinalise")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val hostsService: EventHostsService = EventHostsService.getInstance

    val hostedEventOpt: Observable[HostedEvent] = hostsService
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(he => !he.finalised)
      .map(_.finalise)

    hostedEventOpt.foreach(he => {
      val channel = TextChannelId(he.channelId)
      val message = MessageId(he.messageId)
      hostsService.replace(he)
      BotEnvironment.client.foreach(client => {
        client.requestsHelper.run(EditMessage(channel, message, EditMessageData(JsonOption.fromOptionWithNull(Option(he.asString)))))(msg.cache)
        client.requestsHelper.run(DeleteAllReactions(channel, message))(msg.cache)
      })
    })

    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
