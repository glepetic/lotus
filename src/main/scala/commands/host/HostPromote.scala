package org.maple
package commands.host

import commands.MyCommand
import config.BotEnvironment
import model.HostedEvent
import services.EventHostsService
import utils.IterableUtils.IterableImprovements

import ackcord.JsonOption
import ackcord.commands.UserCommandMessage
import ackcord.data.{MessageId, TextChannelId, User}
import ackcord.requests._

import scala.util.{Failure, Success}

class HostPromote extends MyCommand {
  override def aliases: Seq[String] = Seq("hp", "hostpromote")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {

    Option(arguments)
      .map(arg => arg.join.trim)
      .filter(promotedUserMention => promotedUserMention.matches("<@!?[0-9]+>"))
      .map(_.filter(Character.isDigit))
      .foreach(promotedUserId => {
        val host: User = msg.user
        val hostId: String = host.id.toString
        val hostsService: EventHostsService = EventHostsService.getInstance
        val channelId: String = msg.textChannel.id.toString

        hostsService.findLatest(hostId, channelId)
          .map(he => he.withCohosts(he.cohosts ++ List(promotedUserId)))
          .foreach(he => {
            val futureHostedEventOpt = hostsService.findLatest(promotedUserId, channelId).headOption()

            implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
            futureHostedEventOpt onComplete {
              case Success(hostedEventOpt: Option[HostedEvent]) =>
                hostedEventOpt
                  .filter(!_.finalised)
                  .map(_ => () => BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateDm(CreateDMData(host.id)))(msg.cache)
                    .foreach(createdDm => client.requestsHelper.run(CreateMessage(TextChannelId(createdDm.id), CreateMessageData("The user is already hosting a run in that channel.")))(msg.cache))(client.executionContext)))
                  .getOrElse(() => {
                    hostsService.replace(he)
                    BotEnvironment.client.foreach(client => client.requestsHelper.run(EditMessage(TextChannelId(he.channelId), MessageId(he.messageId), EditMessageData(JsonOption.fromOptionWithNull(Option(he.asString)))))(msg.cache))
                    BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateDm(CreateDMData(host.id)))(msg.cache)
                      .foreach(createdDm => client.requestsHelper.run(CreateMessage(TextChannelId(createdDm.id), CreateMessageData("Done!")))(msg.cache))(client.executionContext))
                  })
                  .apply()
              case Failure(err) => println(err)
            }
          })
      })

    DeleteMessage(msg.textChannel.id, msg.message.id)
  }
}
