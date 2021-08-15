package org.maple
package commands.host

import commands.MyCommand

import ackcord.commands.UserCommandMessage
import ackcord.data.{TextChannelId, User}
import ackcord.requests.{CreateDMData, CreateDm, CreateMessage, CreateMessageData, DeleteMessage, Request}
import org.maple.config.BotEnvironment
import org.maple.services.EventHostsService

class HostPromote extends MyCommand {
  override def aliases: Seq[String] = Seq("hp", "hostpromote")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val host: User = msg.user
    val hostId: String = host.id.toString
    val hostsService: EventHostsService = EventHostsService.getInstance

    hostsService.findLatest(hostId, msg.textChannel.id.toString)
//      .filter(he => !he.finalised && !(he.hostId :: he.cohosts).contains(hostId))
      .map(he => he.withCohosts(he.cohosts ++ List("")))
      .foreach(he => {
        hostsService.replace(he)
        BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateDm(CreateDMData(host.id)))(msg.cache)
          .foreach(createdDm => client.requestsHelper.run(CreateMessage(TextChannelId(createdDm.id), CreateMessageData("Done!")))(msg.cache))(client.executionContext))
      })

    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
