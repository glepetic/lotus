package org.maple
package events.handlers

import config.BotEnvironment
import model.HostedEvent
import services.EventHostsService

import ackcord.data.{MessageId, TextChannelId, ThreadGuildChannelId}
import ackcord.requests._
import ackcord.util.JsonOption
import ackcord.{APIMessage, EventListenerMessage}
import org.mongodb.scala.Observable

import scala.collection.immutable.ListSet
import scala.language.postfixOps

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
      .map(_ => (he: HostedEvent, usrMention: String) => he.withParticipants(he.participants ++ ListSet(usrMention)))
      .orElse(evt.emoji.id
        .filter(id => id.toString equals "871199776572588112")
        .map(_ => (he: HostedEvent, usrMention: String) => he.withParticipants(he.participants.filterNot(_.equalsIgnoreCase(usrMention)))))
      .orElse(evt.emoji.name
        .filter(name => name equalsIgnoreCase "\uD83D\uDC4C")
        .map(_ => (he: HostedEvent, _: String) => evt.user
          .filter(usr => (usr.id.toString equals he.hostId) || (he.cohosts contains usr.id.toString))
          .map(_ => he.finalise)
          .getOrElse(he)
        )
      )
      .getOrElse((he: HostedEvent, _: String) => he)

    val threadActionOpt = (evt.emoji.id, evt.user) match {
      case (id, Some(user)) => id
        .filter(id => id.toString equals "871199809493671978")
        .map(_ => (threadId: String) => AddThreadMember(ThreadGuildChannelId(threadId), user.id))
        .orElse(id
          .filter(id => id.toString equals "871199776572588112")
          .map(_ => (threadId: String) => RemoveThreadMember(ThreadGuildChannelId(threadId), user.id)))
    }

    val hostsService: EventHostsService = EventHostsService.getInstance
    val hostedEventOpt: Observable[HostedEvent] = hostsService
      .find(evt.messageId.toString)

    hostedEventOpt.foreach(he => {
      val updatedHostedEventOpt = evt.user.filter(_ => !he.finalised).map(usr => mapper(he, usr.mentionNick))
      updatedHostedEventOpt.foreach(hostsService.replace)
      val hostedEventAsString = updatedHostedEventOpt.map(_.asString)
      val channelId = TextChannelId(he.channelId)
      val messageId = MessageId(he.messageId)
      val request = updatedHostedEventOpt
        .filter(_.finalised)
        .map(_ => DeleteAllReactions(channelId, messageId))
        .getOrElse(DeleteUserReaction(channelId, messageId, emoji, evt.userId))
      BotEnvironment.client.foreach(client => {
        threadActionOpt.foreach(threadAction => client.requestsHelper.run(threadAction.apply(he.threadId))(cache))
        client.requestsHelper.run(EditMessage(channelId, messageId, EditMessageData(JsonOption.fromOptionWithNull(hostedEventAsString))))(cache)
        client.requestsHelper.run(request)(cache)
      })
    })

  }

}
