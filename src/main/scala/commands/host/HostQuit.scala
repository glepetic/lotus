package org.maple
package commands.host

import commands.MyCommand
import services.EventHostsService

import ackcord.commands.UserCommandMessage
import ackcord.data.User
import ackcord.requests.{DeleteMessage, Request}

class HostQuit extends MyCommand {
  override def aliases: Seq[String] = Seq("hq","hostquit")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val hostsService: EventHostsService = EventHostsService.getInstance

    hostsService.findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(he => !he.finalised)
//      .map(he => he.wi)

    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
