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
                       threadId: String,
                       description: String,
                       id: String = UUID.randomUUID().toString,
                       cohosts: ListSet[String] = ListSet.empty,
                       participants: ListSet[String] = ListSet.empty,
                       finalised: Boolean = false) {

  def asString: String = s"<@$hostId> is hosting an event in channel <#$channelId>.\n\n$descriptionBody\n\n$cohostsAsString\n\n$participantsAsString\n$footerDescription"

  def descriptionBody: String = Markdown.bold("Description") + "\n" + Option(description).filter(_.nonEmpty).getOrElse("None")

  def cohostsAsString: String = Markdown.bold(s"Cohosts (${cohosts.size})") + "\n" +
    Option(cohostsAsMentions)
      .filter(ch => ch.nonEmpty)
      .map(_.joinWords)
      .getOrElse("None")

  def participantsAsString: String = Markdown.bold(s"Roster (${participants.size})") + "\n" +
    Option(participants.zipWithIndex)
      .filter(p => p.nonEmpty)
      .map(p => p.map { case (mention, index) => s"${index + 1}. $mention" })
      .map(_.joinLines)
      .getOrElse("None\n")

  def footerDescription: String = Option(finalised)
    .filter(f => !f)
    .map(_ => this.runOptions)
    .getOrElse("Registrations for this event have been closed.")

  def runOptions: String = Markdown.bold("Options") + "\n" +
    "- React with <:greencheck:871199809493671978> to be added to the roster" + "\n" +
    "- React with <:redx:871199776572588112> to be removed from the roster" + "\n" +
    s"- React with \uD83D\uDC4C to finalise the roster - ONLY HOST" + "\n" + "\n" +
    "--"


  def cohostsAsMentions: ListSet[String] = this.cohosts.map(asMention)
  def asMention(id: String): String = s"<@!$id>"
  def mentions: String = this.participants.filter(_.matches("<@!?[0-9]+>")).joinWords

  def isLonelyHost: Boolean = (this.cohosts.size + 1) equals 1

  def withoutHost(hId: String): HostedEvent = Option(this.hostId)
    .filter(_.equalsIgnoreCase(hId))
    .flatMap(_ => this.cohosts.headOption)
    .map(headCohostId => this.withoutCohost(headCohostId).withHost(headCohostId))
    .getOrElse(this.withoutCohost(hId))

  private def withoutCohost(id: String) = this.withCohosts(this.cohosts.filterNot(_.equalsIgnoreCase(id)))

  def withHost(hostId: String): HostedEvent = HostedEvent(this.messageId, this.timestamp, hostId, this.channelId, this.threadId, this.description, this.id, this.cohosts, this.participants, this.finalised)

  def withCohosts(newCohosts: ListSet[String]): HostedEvent = HostedEvent(this.messageId, this.timestamp, this.hostId, this.channelId, this.threadId, this.description, this.id, newCohosts, this.participants, this.finalised)

  def withDescription(newDescription: String): HostedEvent = HostedEvent(this.messageId, this.timestamp, this.hostId, this.channelId, this.threadId, newDescription, this.id, this.cohosts, this.participants, this.finalised)

  def withMessageId(newMessageId: String): HostedEvent = HostedEvent(newMessageId, this.timestamp, this.hostId, this.channelId, this.threadId, this.description, this.id, this.cohosts, this.participants, this.finalised)

  def withThreadId(newThreadId: String): HostedEvent = HostedEvent(this.messageId, this.timestamp, this.hostId, this.channelId, newThreadId, this.description, this.id, this.cohosts, this.participants, this.finalised)

  def withParticipants(newParticipants: ListSet[String]): HostedEvent = HostedEvent(this.messageId, this.timestamp, this.hostId, this.channelId, this.threadId, this.description, this.id, this.cohosts, newParticipants, this.finalised)

  def withFinalised(isFinalised: Boolean): HostedEvent = HostedEvent(this.messageId, this.timestamp, this.hostId, this.channelId, this.threadId, this.description, this.id, this.cohosts, this.participants, isFinalised)

  def finalise: HostedEvent = this.withFinalised(true)
}
