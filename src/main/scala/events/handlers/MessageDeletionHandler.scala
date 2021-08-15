package org.maple
package events.handlers

import services.EventHostsService

import ackcord.{APIMessage, EventListenerMessage}

object MessageDeletionHandler {

  def handle(msgDeleteEvt: EventListenerMessage[APIMessage.MessageDelete]): Unit = {
    val evt = msgDeleteEvt.event
    val hostsService: EventHostsService = EventHostsService.getInstance
    hostsService.find(evt.messageId.toString).filter(!_.finalised).map(_.finalise).foreach(hostsService.replace)
  }

}
