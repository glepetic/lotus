package org.maple
package commands.storymaple.bossing

import builders.EmbedBuilder
import commands.MyCommand
import services.BossRunsService
import utils.IterableUtils._
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, Request}
import org.maple.dto.RaiderDto

class BossRoster extends MyCommand {
  override def aliases: Seq[String] = Seq("br","bossroster")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val bossRunsService = new BossRunsService()

    val bossRoster = Option(bossRunsService.getRoster(arguments))
      .filter(roster => roster.hasRaiders)

    val embedBuilder = EmbedBuilder
      .builder
      .authorRequestedBy(msg.user)
      .defaultFooter

    val embed = bossRoster.map(br => embedBuilder
      .defaultColor
      .defaultThumbnail
      .title("Boss Roster")
      .withOptField("Party 1", this.partyAsStringOpt(br.party1Raiders))
      .withOptField("Party 2", this.partyAsStringOpt(br.party2Raiders))
      .withOptField("Fillers", this.partyAsStringOpt(br.fillers))
      .withField("Jobs", br.jobs.map(j => s"${j.name} - ${Markdown.bold(j.count.toString)}").joinLines)
      .withField("Unique Jobs", br.uniqueJobsCount.toString, inline = true)
      .withField("Total Characters", br.totalCharacters.toString, inline = true)
      .build
    ).getOrElse(embedBuilder.defaultErrorColor.defaultErrorThumbnail.title("Boss Roster Error").description(s"Could not retrieve data for ${Markdown.bold(arguments.joinWords)}").build)

    CreateMessage(msg.textChannel.id, CreateMessageData(embed = Option(embed)))
  }

  private def partyAsStringOpt(party: List[RaiderDto]): Option[String] = Option(party).filter(_.nonEmpty).map(this.partyAsString)
  private def partyAsString(party: List[RaiderDto]): String = Markdown.codeSnippet(party.map(r => s"${this.completeSpaces(r.ign, 14)}${this.completeSpaces(r.level.toString, 5)}${this.completeSpaces(r.job.name, 14)}${s"[${this.completeSpaces(r.links.toString, 4)} Links]"}").joinLines)
  private def completeSpaces(content: String, n: Int): String = content + (1 to n-content.length).map(_ => " ").join

}
