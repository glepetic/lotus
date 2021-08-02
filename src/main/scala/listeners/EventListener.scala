package org.maple
package listeners

import events.handlers.ReactionsHandler

import ackcord.{APIMessage, EventsController, Requests}


class EventListener(requests: Requests) extends EventsController(requests) {
  val onReady = Event.on[APIMessage.Ready].withSideEffects(_ => println("Bot Online!"))
  val onReaction = Event.on[APIMessage.MessageReactionAdd].withSideEffects(ReactionsHandler.handle)
}
