package org.maple
package commands.host

import commands.MyCommand
import config.BotEnvironment
import services.EventHostsService

import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, User}
import ackcord.requests.{CreateReaction, DeleteMessage, Request}
import ackcord.syntax.TextChannelSyntax
import org.maple.model.HostedEvent
import org.mongodb.scala.Observable

class HostRepeat extends MyCommand {
  override def aliases: Seq[String] = Seq("hr", "hostrepeat")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val hostsService: EventHostsService = EventHostsService.getInstance

    val hostedEventOpt: Observable[HostedEvent] = hostsService
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(he => !he.finalised)

    hostedEventOpt.foreach(he => {
      BotEnvironment.client.foreach(client => client.requestsHelper.run(msg.textChannel.sendMessage(he.asString))(msg.cache).foreach(sentMsg => {
        hostsService.replace(he.withMessageId(sentMsg.id.toString))
        client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "greencheck:871199809493671978"))(msg.cache)
          .foreach(_ => client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "redx:871199776572588112"))(msg.cache)
            .foreach(_ => client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "\uD83D\uDC4C"))(msg.cache)
              .foreach(_ => client.requestsHelper.run(DeleteMessage(msg.textChannel.id, MessageId(he.messageId)))(msg.cache))(client.executionContext)
            )(client.executionContext)
          )(client.executionContext)
      })(client.executionContext))
    })
    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
