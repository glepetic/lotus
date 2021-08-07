package org.maple
package commands.storymaple.bossing

import commands.MyCommand
import config.BotEnvironment
import model.maplestory.BossRun
import repositories.HostsRepository
import utils.IterableUtils.IterableImprovements

import ackcord.commands.UserCommandMessage
import ackcord.data.User
import ackcord.requests.{CreateMessage, CreateMessageData, CreateReaction, Request}
import ackcord.syntax.TextChannelSyntax
import org.maple.services.EventHostsService

import java.time.Instant
import scala.util.{Failure, Success}

class Host extends MyCommand {
  override def aliases: Seq[String] = Seq("h", "host")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val description: String = arguments.joinWords.trim
    val bossRun: BossRun = BossRun(msg.message.id.toString, Instant.now, host.id.toString, msg.textChannel.id.toString, description)
    val hostsService: EventHostsService = EventHostsService.getInstance

    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
    hostsService
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .toFutureOption() onComplete {
      case Success(maybeBr) => maybeBr
        .filter(br => !br.finalised)
        .map(_ => () => BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateMessage(msg.textChannel.id, CreateMessageData("You are already hosting a run on this channel. End your previous one before hosting a new one!")))(msg.cache)))
        .getOrElse(() => BotEnvironment.client.foreach(client => client.requestsHelper.run(msg.textChannel.sendMessage(bossRun.asString))(msg.cache)
          .foreach(sentMsg => {
            hostsService.insert(BossRun(sentMsg.id.toString, Instant.now, host.id.toString, sentMsg.channelId.toString, description))
            client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "greencheck:871199809493671978"))(msg.cache)
              .foreach(_ => client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "redx:871199776572588112"))(msg.cache)
                .foreach(_ => client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "\uD83D\uDC4C"))(msg.cache))(client.executionContext)
              )(client.executionContext)
          })(client.executionContext)))
        .apply()
      case Failure(e) => throw e
    }

    null
  }
}
