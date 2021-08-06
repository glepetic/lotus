package org.maple
package commands.storymaple.bossing

import commands.MyCommand
import config.BotEnvironment
import model.maplestory.BossRun
import repositories.HostsRepository

import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, User}
import ackcord.requests.{CreateReaction, DeleteMessage, Request}
import ackcord.syntax.TextChannelSyntax

class HostRepeat extends MyCommand {
  override def aliases: Seq[String] = Seq("hr", "hostrepeat")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val hostsRepository: HostsRepository = HostsRepository.getInstance

    val maybeBossRun: Option[BossRun] = hostsRepository
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(br => !br.finalised)

    maybeBossRun.foreach(br => {
      BotEnvironment.client.foreach(client => client.requestsHelper.run(msg.textChannel.sendMessage(br.asString))(msg.cache).foreach(sentMsg => {
        hostsRepository.update(BossRun(sentMsg.id.toString, br.timestamp, br.hostId, br.channelId, br.description, br.id, br.mentions))
        client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "greencheck:871199809493671978"))(msg.cache)
          .foreach(_ => client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "redx:871199776572588112"))(msg.cache)
            .foreach(_ => client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "\uD83D\uDC4C"))(msg.cache)
              .foreach(_ => client.requestsHelper.run(DeleteMessage(msg.textChannel.id, MessageId(br.messageId)))(msg.cache))(client.executionContext)
            )(client.executionContext)
          )(client.executionContext)
      })(client.executionContext))
    })
    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
