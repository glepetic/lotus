package org.maple
package events.handlers

import config.BotEnvironment
import model.maplestory.BossRun
import services.EventHostsService

import ackcord.data.{MessageId, TextChannelId}
import ackcord.requests.{DeleteAllReactions, DeleteUserReaction, EditMessage, EditMessageData}
import ackcord.util.JsonOption
import ackcord.{APIMessage, EventListenerMessage}
import org.mongodb.scala.Observable

import scala.collection.immutable.ListSet

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
    val mapper = evt.emoji.id
      .filter(id => id.toString equals "871199809493671978")
      .map(_ => (br: BossRun, usrMention: String) => BossRun(br.messageId, br.timestamp, br.hostId, br.channelId, br.description, br.id, br.mentions ++ ListSet(usrMention)))
      .orElse(evt.emoji.id
        .filter(id => id.toString equals "871199776572588112")
        .map(_ => (br: BossRun, usrMention: String) => BossRun(br.messageId, br.timestamp, br.hostId, br.channelId, br.description, br.id, br.mentions.filterNot(_.equalsIgnoreCase(usrMention)))))
      .orElse(evt.emoji.name
        .filter(name => name equalsIgnoreCase "\uD83D\uDC4C")
        .map(_ => (br: BossRun, _: String) => evt.user.filter(usr => usr.id.toString equals br.hostId).map(_ => br.finalise).getOrElse(br))
      )
      .getOrElse((br: BossRun, _: String) => br)

    val hostsService: EventHostsService = EventHostsService.getInstance
    val maybeBossRun: Observable[BossRun] = hostsService
      .find(evt.messageId.toString)

    maybeBossRun.foreach(br => {
      val maybeUpdatedBossRun = evt.user.filter(_ => !br.finalised).map(usr => mapper(br, usr.mentionNick))
      maybeUpdatedBossRun.foreach(hostsService.replace)
      val bossRunAsString = maybeUpdatedBossRun.map(_.asString)
      val channelId = TextChannelId(br.channelId)
      val messageId = MessageId(br.messageId)
      val request = maybeUpdatedBossRun
        .filter(_.finalised)
        .map(_ => DeleteAllReactions(channelId, messageId))
        .getOrElse(DeleteUserReaction(channelId, messageId, emoji, evt.userId))
      BotEnvironment.client.foreach(client => {
        client.requestsHelper.run(EditMessage(channelId, messageId, EditMessageData(JsonOption.fromOptionWithNull(bossRunAsString))))(cache)
        client.requestsHelper.run(request)(cache)
      })
    })

  }

}
