package org.maple
package model.maplestory

import utils.IterableUtils.IterableImprovements
import utils.discord.Markdown

import scala.collection.immutable.ListSet

case class BossRun(messageId: String, hostId: String, channelId: String, description: String, mentions: ListSet[String] = ListSet.empty) {
  def asString: String = s"<@$hostId> is hosting ${Markdown.hightlight(description)} in channel <#$channelId>.\n\n$mentionsAsString\nPlease react to be added to the roster."
  def mentionsAsString: String = Markdown.bold(s"Roster (${mentions.size})") + "\n" +
    Option(mentions.zipWithIndex)
      .filter(mts => mts.nonEmpty)
      .map(mts => mts.map { case (mention, index) => s"${index + 1}. $mention" })
      .map(_.joinLines)
      .getOrElse("None\n")
}
