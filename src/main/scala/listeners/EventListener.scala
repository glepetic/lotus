package org.maple
package listeners

import clients.MyDiscordClient

import ackcord.data.{GuildId, TextChannelId}
import ackcord.requests.{CreateMessage, CreateMessageData}
import ackcord.{APIMessage, EventsController, Requests}

import scala.util.{Failure, Success}


class EventListener(requests: Requests) extends EventsController(requests) {
  val onReady = Event.on[APIMessage.Ready].withSideEffects(evt => {
    println("Bot Online!")
    val discClient = MyDiscordClient.withCache(evt.cacheSnapshot)
    val x = discClient.getGuildChannels(GuildId("793470660688216145")).value
    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
    x onComplete {
      case Success(a) => {
        val y = a.flatMap(z => z.find(ch => ch.name.map(_.toLowerCase) contains "royale")).flatMap(ch => ch.toGuildChannel(GuildId("793470660688216145")))
        y.foreach(ch => discClient.run(CreateMessage(ch.id.asInstanceOf[TextChannelId], CreateMessageData("Royale in 15 minutes"))))
      }
      case Failure(e) => throw e
    }
  }
  )
}
