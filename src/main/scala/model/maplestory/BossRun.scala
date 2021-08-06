package org.maple
package model.maplestory

import config.BotEnvironment
import utils.IterableUtils.IterableImprovements
import utils.discord.Markdown

import java.time.Instant
import java.util.UUID
import scala.collection.immutable.ListSet

case class BossRun(messageId: String, timestamp: Instant, hostId: String, channelId: String, description: String, id: String = UUID.randomUUID().toString, mentions: ListSet[String] = ListSet.empty, finalised: Boolean = false) {

  def asString: String = s"<@$hostId> is hosting an event in channel <#$channelId>.\n\n$descriptionBody\n\n$mentionsAsString\n$footerDescription"

  def descriptionBody: String = Markdown.bold("Description") + "\n" + description

  def mentionsAsString: String = Markdown.bold(s"Roster (${mentions.size})") + "\n" +
    Option(mentions.zipWithIndex)
      .filter(mts => mts.nonEmpty)
      .map(mts => mts.map { case (mention, index) => s"${index + 1}. $mention" })
      .map(_.joinLines)
      .getOrElse("None\n")

  def footerDescription: String = Option(finalised)
    .filter(f => !f)
    .map(_ => this.runOptions)
    .getOrElse("This event has ended.")

  def runOptions: String = Markdown.bold("Options") + "\n" +
    "- React with <:greencheck:871199809493671978> to be added to the roster" + "\n" +
    "- React with <:redx:871199776572588112> to be removed from the roster" + "\n" + "\n" +
    Markdown.bold("Host Options") + "\n" +
    "- React with \uD83D\uDC4C to finalise the roster" + "\n" +
    s"- Use command ${Markdown.hightlight(BotEnvironment.prefix + "ha [names/mentions]")} to add other people to the roster. Example: ${Markdown.hightlight(BotEnvironment.prefix + "ha ahri @Gon @Franco alex")}" + "\n" +
    s"- Use command ${Markdown.hightlight(BotEnvironment.prefix + "hk [numbers in roster]")} to remove people from the roster. Example: ${Markdown.hightlight(BotEnvironment.prefix + "hk 1 3")}" + "\n" +
    s"- Use command ${Markdown.hightlight(BotEnvironment.prefix + "hdm [new description]")} to modify the description of the event." + "\n" +
    s"- Use command ${Markdown.hightlight(BotEnvironment.prefix + "hf")} to end the event and close registrations." + "\n" +
    s"- Use command ${Markdown.hightlight(BotEnvironment.prefix + "hr")} to repeat the event message in the channel." + "\n" + "\n" +
    "--"

  def finalise: BossRun = BossRun(this.messageId, this.timestamp, this.hostId, this.channelId, this.description, this.id, this.mentions, finalised = true)
}