package org.maple
package listeners

import ackcord.{APIMessage, EventsController, Requests}


class EventListener(requests: Requests) extends EventsController(requests) {
  val onReady = Event.on[APIMessage.Ready].withSideEffects(_ => println("Bot Online!"))
}
