package org.maple
package events.handlers

import repositories.HostsRepository

import ackcord.{APIMessage, EventListenerMessage}

object MessageDeletionHandler {

  def handle(msgDeleteEvt: EventListenerMessage[APIMessage.MessageDelete]): Unit = {
    val evt = msgDeleteEvt.event
    val hostsRepository: HostsRepository = HostsRepository.getInstance
    hostsRepository.find(evt.messageId.toString).map(_.finalise).foreach(hostsRepository.update)
  }

}
