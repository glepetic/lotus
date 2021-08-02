package org.maple
package events.handlers

import model.maplestory.BossRun
import repositories.HostsRepository

import ackcord.data.{MessageId, TextChannelId}
import ackcord.requests.{DeleteUserReaction, EditMessage, EditMessageData, GetChannelMessage}
import ackcord.util.JsonOption
import ackcord.{APIMessage, EventListenerMessage}
import org.maple.config.BotEnvironment

object ReactionsHandler {

  def handle(reactionEvt: EventListenerMessage[APIMessage.MessageReactionAdd]): Unit = {
    val evt = reactionEvt.event
    val cache = reactionEvt.cacheSnapshot
    if (evt.user.flatMap(_.bot).getOrElse(false)) return
    val emoji = (evt.emoji.id, evt.emoji.name) match {
      case (Some(id), Some(name)) => s"$name:${id.toString}"
      case (None, Some(name)) => name
      case _ => ""
    }
    val action = evt.emoji.id
      .filter(id => id.toString equals "871199809493671978")
      .map(_ => (mentions: Set[String], usrMention: String) => mentions ++ Set(usrMention))
      .orElse(evt.emoji.id
        .filter(id => id.toString equals "871199776572588112")
        .map(_ => (mentions: Set[String], usrMention: String) => mentions.filterNot(_.equalsIgnoreCase(usrMention))))
      .getOrElse((mentions: Set[String], _: String) => mentions)

    val hostsRepository: HostsRepository = HostsRepository.getInstance
    val maybeBossRun: Option[BossRun] = hostsRepository.find(evt.messageId.toString)

    maybeBossRun.foreach(br => {
      val updatedBossRun = evt.user
        .map(usr => BossRun(br.messageId, br.hostId, br.channelId, br.description, action(br.mentions, usr.mention)))
      updatedBossRun.foreach(hostsRepository.update)
      val bossRunAsString = updatedBossRun.map(_.asString)
      val channelId = TextChannelId(br.channelId)
      val messageId = MessageId(br.messageId)
      BotEnvironment.client.foreach(client => client.requestsHelper.run(GetChannelMessage(channelId, messageId))(cache)
        .foreach(_ => client.requestsHelper.run(EditMessage(channelId, messageId, EditMessageData(JsonOption.fromOptionWithNull(bossRunAsString))))(cache)
          .foreach(_ => client.requestsHelper.run(DeleteUserReaction(channelId, messageId, emoji, evt.userId))(cache))(client.executionContext))(client.executionContext))
    })

  }

}
