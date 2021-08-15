package org.maple
package model

import config.BotEnvironment
import utils.IterableUtils.IterableImprovements
import utils.discord.Markdown

import java.time.Instant
import java.util.UUID
import scala.collection.immutable.ListSet

case class HostedEvent(messageId: String,
                       timestamp: Instant,
                       hostId: String,
                       channelId: String,
                       description: String,
                       id: String = UUID.randomUUID().toString,
                       cohosts: List[String] = Nil,
                       participants: ListSet[String] = ListSet.empty,
                       finalised: Boolean = false) {

  def asString: String = s"<@$hostId> is hosting an event in channel <#$channelId>.\n\n$descriptionBody\n\n$participantsAsString\n$footerDescription"

  def descriptionBody: String = Markdown.bold("Description") + "\n" + description

  def participantsAsString: String = Markdown.bold(s"Roster (${participants.size})") + "\n" +
    Option(participants.zipWithIndex)
      .filter(mts => mts.nonEmpty)
      .map(mts => mts.map { case (mention, index) => s"${index + 1}. $mention" })
      .map(_.joinLines)
      .getOrElse("None\n")

  def footerDescription: String = Option(finalised)
    .filter(f => !f)
    .map(_ => this.runOptions)
    .getOrElse("Registrations for this event have been closed.")

  def runOptions: String = Markdown.bold("Options") + "\n" +
    "- React with <:greencheck:871199809493671978> to be added to the roster" + "\n" +
    "- React with <:redx:871199776572588112> to be removed from the roster" + "\n" + "\n" +
    Markdown.bold("Host Options") + "\n" +
    "- React with \uD83D\uDC4C to finalise the roster" + "\n" +
    s"- Use command ${Markdown.hightlight(BotEnvironment.prefix + "ha [names/mentions]")} to add other people to the roster. Please consider that names/mentions must be separated by ${Markdown.hightlight("++")} Example: ${Markdown.hightlight(BotEnvironment.prefix + "ha Ahri - BM/NL ++ @Gon ++ ghost")}" + "\n" +
    s"- Use command ${Markdown.hightlight(BotEnvironment.prefix + "hk [numbers in roster]")} to remove people from the roster. Example: ${Markdown.hightlight(BotEnvironment.prefix + "hk 1 3")}" + "\n" +
    s"- Use command ${Markdown.hightlight(BotEnvironment.prefix + "hdm [new description]")} to modify the description of the event." + "\n" +
    s"- Use command ${Markdown.hightlight(BotEnvironment.prefix + "hf")} to end the event and close registrations." + "\n" +
    s"- Use command ${Markdown.hightlight(BotEnvironment.prefix + "hr")} to repeat the event message in the channel." + "\n" + "\n" +
    "--"

  def mentions: String = this.participants.filter(_.matches("<@![0-9]+>")).joinWords

  def withCohosts(newCohosts: List[String]): HostedEvent = HostedEvent(this.messageId, this.timestamp, this.hostId, this.channelId, this.description, this.id, newCohosts, this.participants, this.finalised)

  def withDescription(newDescription: String): HostedEvent = HostedEvent(this.messageId, this.timestamp, this.hostId, this.channelId, newDescription, this.id, this.cohosts, this.participants, this.finalised)

  def withMessageId(newMessageId: String): HostedEvent = HostedEvent(newMessageId, this.timestamp, this.hostId, this.channelId, this.description, this.id, this.cohosts, this.participants, this.finalised)

  def withParticipants(newParticipants: ListSet[String]): HostedEvent = HostedEvent(this.messageId, this.timestamp, this.hostId, this.channelId, this.description, this.id, this.cohosts, newParticipants, this.finalised)

  def withFinalised(isFinalised: Boolean): HostedEvent = HostedEvent(this.messageId, this.timestamp, this.hostId, this.channelId, this.description, this.id, this.cohosts, this.participants, isFinalised)

  def finalise: HostedEvent = this.withFinalised(true)
}
