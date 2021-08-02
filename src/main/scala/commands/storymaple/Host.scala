package org.maple
package commands.storymaple

import commands.MyCommand
import config.BotEnvironment
import model.maplestory.BossRun
import repositories.HostsRepository
import utils.IterableUtils.IterableImprovements
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.data.User
import ackcord.requests.{CreateReaction, Request}
import ackcord.syntax.TextChannelSyntax

class Host extends MyCommand {
  override def aliases: Seq[String] = Seq("h","host")
  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val description: String = arguments.joinWords.trim
    val bossRun: BossRun = BossRun(msg.message.id.toString, host.id.toString, msg.textChannel.id.toString, description)
    BotEnvironment.client.foreach(client => client.requestsHelper.run(msg.textChannel.sendMessage(bossRun.asString))(msg.cache).foreach(sentMsg => {
      val hostsRepository: HostsRepository = HostsRepository.getInstance
      hostsRepository.insert(BossRun(sentMsg.id.toString, host.id.toString, sentMsg.channelId.toString, description))
      client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "greencheck:871199809493671978"))(msg.cache)
        .foreach(_ => client.requestsHelper.run(CreateReaction(sentMsg.channelId, sentMsg.id, "redx:871199776572588112"))(msg.cache))(client.executionContext)
    })(client.executionContext))
    null
  }
}
