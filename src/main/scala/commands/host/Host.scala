package org.maple
package commands.host

import commands.MyCommand
import config.BotEnvironment
import services.EventHostsService
import utils.IterableUtils.IterableImprovements

import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, User}
import ackcord.requests.{CreateMessage, CreateMessageData, CreateReaction, DeleteMessage, Request}
import ackcord.syntax.TextChannelSyntax
import org.maple.model
import org.maple.model.HostedEvent

import java.time.Instant
import scala.util.{Failure, Success}

class Host extends MyCommand {
  override def aliases: Seq[String] = Seq("h", "host")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val description: String = arguments.joinWords.trim
    val hostedEvent: HostedEvent = model.HostedEvent(msg.message.id.toString, Instant.now, host.id.toString, msg.textChannel.id.toString, description)
    val hostsService: EventHostsService = EventHostsService.getInstance

    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
    hostsService
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .toFutureOption() onComplete {
      case Success(hostedEventOpt) => hostedEventOpt
        .filter(he => !he.finalised)
        .map(he => () => BotEnvironment.client
          .foreach(client => client.requestsHelper.run(
            CreateMessage(msg.textChannel.id,
              CreateMessageData("You are already hosting or cohosting a run on this channel. Either resign as a host or end your previous event before hosting a new one!", replyTo = Option(MessageId(he.messageId)))))(msg.cache)))
        .getOrElse(() => BotEnvironment.client.foreach(client => client.requestsHelper.run(msg.textChannel.sendMessage(hostedEvent.asString))(msg.cache)
          .foreach(sentMsg => {
            hostsService.insert(model.HostedEvent(sentMsg.id.toString, Instant.now, host.id.toString, sentMsg.channelId.toString, description))
            client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "greencheck:871199809493671978"))(msg.cache)
              .foreach(_ => client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "redx:871199776572588112"))(msg.cache)
                .foreach(_ => client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "\uD83D\uDC4C"))(msg.cache))(client.executionContext)
              )(client.executionContext)
          })(client.executionContext)))
        .apply()
      case Failure(e) => throw e
    }

    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
