package org.maple
package commands.host

import commands.MyCommand
import config.BotEnvironment
import services.EventHostsService

import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, TextChannelId, User}
import ackcord.requests._
import ackcord.util.JsonOption

class HostQuit extends MyCommand {
  override def aliases: Seq[String] = Seq("hq", "hostquit")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val hostId: String = host.id.toString
    val hostsService: EventHostsService = EventHostsService.getInstance
    val textChannelId = msg.textChannel.id

    hostsService.findLatest(hostId, textChannelId.toString)
      .foreach(he => Option(he)
        .filter(_.finalised)
        .map(_ => () => BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateMessage(textChannelId, CreateMessageData("You are not currently hosting or cohosting an event on this channel.")))(msg.cache)))
        .orElse(Option(he)
          .filter(_.isLonelyHost)
          .map(_ => () => BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateMessage(textChannelId, CreateMessageData("You cannot quit as host because you are the only host for your event.")))(msg.cache))))
        .getOrElse(() => {
          val updatedHostedEvent = he.withoutHost(hostId)
          hostsService.replace(updatedHostedEvent)
          BotEnvironment.client.foreach(client => client.requestsHelper.run(EditMessage(TextChannelId(updatedHostedEvent.channelId), MessageId(updatedHostedEvent.messageId), EditMessageData(JsonOption.fromOptionWithNull(Option(updatedHostedEvent.asString)))))(msg.cache))
        })
        .apply()
      )

    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
