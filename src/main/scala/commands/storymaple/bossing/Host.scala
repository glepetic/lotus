package org.maple
package commands.storymaple.bossing

import commands.MyCommand
import config.BotEnvironment
import model.maplestory.BossRun
import repositories.HostsRepository
import utils.IterableUtils.IterableImprovements

import ackcord.commands.UserCommandMessage
import ackcord.data.User
import ackcord.requests.{CreateReaction, Request}
import ackcord.syntax.TextChannelSyntax

import java.time.Instant

class Host extends MyCommand {
  override def aliases: Seq[String] = Seq("h", "host")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val description: String = arguments.joinWords.trim
    val bossRun: BossRun = BossRun(msg.message.id.toString, Instant.now, host.id.toString, msg.textChannel.id.toString, description)
    val hostsRepository: HostsRepository = HostsRepository.getInstance
    if (hostsRepository.findLatest(host.id.toString, msg.textChannel.id.toString).exists(br => !br.finalised))
      return msg.textChannel.sendMessage("You are already hosting a run on this channel. End your previous one before hosting a new one!")
    BotEnvironment.client.foreach(client => client.requestsHelper.run(msg.textChannel.sendMessage(bossRun.asString))(msg.cache).foreach(sentMsg => {
      hostsRepository.insert(BossRun(sentMsg.id.toString, Instant.now, host.id.toString, sentMsg.channelId.toString, description))
      client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "greencheck:871199809493671978"))(msg.cache)
        .foreach(_ => client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "redx:871199776572588112"))(msg.cache)
          .foreach(_ => client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "\uD83D\uDC4C"))(msg.cache))(client.executionContext)
        )(client.executionContext)
    })(client.executionContext))
    null
  }
}
