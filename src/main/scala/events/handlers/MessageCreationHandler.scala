package org.maple
package events.handlers

import commands.MyCommand
import config.BotEnvironment
import services.CommandsService
import utils.OptionUtils.OptionImprovements

import ackcord.commands.{CommandMessage, UserCommandMessage}
import ackcord.data.TextChannel
import ackcord.requests.GetChannel
import ackcord.{APIMessage, EventListenerMessage}
import akka.NotUsed

object MessageCreationHandler {

  def handle(msgCreateEvt: EventListenerMessage[APIMessage.MessageCreate]): Unit = {
    val evt = msgCreateEvt.event

    val splitContent = evt.message.content.split(" ")

    val myCommand: MyCommand = Option(splitContent)
      .filter(_.nonEmpty)
      .map(_.head.filterNot(_.equals(BotEnvironment.prefix)))
      .flatMap(CommandsService.getInstance.get)
      .orThrow

    val arguments: List[String] = Option(splitContent)
      .filter(_.length > 1)
      .map(_.tail.toList)
      .getOrElse(Nil)

    BotEnvironment.client
      .foreach(client => client.requestsHelper.run(GetChannel(evt.message.channelId))(msgCreateEvt.cacheSnapshot)
        .filter(retrievedCh => retrievedCh.nsfw.getOrElse(false))
        .map(retrievedCh => UserCommandMessage.Default.apply(evt.message.authorUser(msgCreateEvt.cacheSnapshot).orThrow,
          CommandMessage.Default.apply(client.requests, msgCreateEvt.cacheSnapshot, retrievedCh.asInstanceOf[TextChannel], evt.message, NotUsed)))(client.executionContext)
        .foreach(ucm => myCommand.execute(ucm, arguments))(client.executionContext))
  }

}
