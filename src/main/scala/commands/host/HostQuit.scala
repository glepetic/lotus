package org.maple
package commands.host

import commands.MyCommand
import services.EventHostsService

import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, TextChannelId, User}
import ackcord.requests.{CreateDMData, CreateDm, CreateMessage, CreateMessageData, DeleteMessage, EditMessage, EditMessageData, Request}
import ackcord.util.JsonOption
import org.maple.config.BotEnvironment
import org.maple.utils.IterableUtils.IterableImprovements

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
        .map(_ => () => BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateMessage(textChannelId, CreateMessageData("The run has already been finalised.")))(msg.cache)))
        .orElse(Option(he)
          .filter(_.isLonelyHost)
          .map(_ => () => BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateMessage(textChannelId, CreateMessageData("You cannot quit as host because you are the only host.")))(msg.cache))))
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
