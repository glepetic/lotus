package org.maple
package commands.host

import commands.MyCommand
import config.BotEnvironment
import services.EventHostsService
import utils.IterableUtils.IterableImprovements

import ackcord.commands.UserCommandMessage
import ackcord.data.User
import ackcord.requests.{CreateMessage, CreateMessageData, DeleteMessage, Request}

class HostMention extends MyCommand {
  override def aliases: Seq[String] = Seq("hm","hostmention")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val contentToSend: String = arguments.joinWords.trim
    val host: User = msg.user
    val hostsService: EventHostsService = EventHostsService.getInstance
    hostsService
      .findLatest(host.id.toString, msg.textChannel.id.toString)
      .filter(!_.finalised)
      .map(_.mentions + "\n\n" + contentToSend)
      .foreach(content => BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateMessage(msg.textChannel.id, CreateMessageData(content)))(msg.cache)))

    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
