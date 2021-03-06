package org.maple
package commands.storymaple

import builders.EmbedBuilder
import commands.MyCommand
import dto.RaiderDto
import services.BossRunsService
import utils.IterableUtils._
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, Request}

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
      .withOptField(s"Party 1  -  (${br.party1Raiders.length}/6)", this.partyAsStringOpt(br.party1Raiders))
      .withOptField(s"Party 2  -  (${br.party2Raiders.length}/6)", this.partyAsStringOpt(br.party2Raiders))
      .withOptField(s"Fillers  -  (${br.fillers.length})", this.partyAsStringOpt(br.fillers))
      .withField("Jobs", br.jobs.map(j => s"${j.name} - ${Markdown.bold(j.count.toString)}").joinLines)
      .withField("Unique Jobs", br.uniqueJobsCount.toString, inline = true)
      .withField("Total Characters", br.totalCharacters.toString, inline = true)
      .build
    ).getOrElse(embedBuilder.defaultErrorColor.defaultErrorThumbnail.title("Boss Roster Error").description(s"Could not retrieve data for ${Markdown.bold(arguments.joinWords)}").build)

    CreateMessage(msg.textChannel.id, CreateMessageData(embeds = Seq(embed)))
  }

  private def partyAsStringOpt(party: List[RaiderDto]): Option[String] = Option(party).filter(_.nonEmpty).map(this.partyAsString)
  private def partyAsString(party: List[RaiderDto]): String = Markdown.codeSnippet(party.map(r => s"${this.completeSpaces(r.ign, 14)}${this.completeSpaces(r.level.toString, 5)}${this.completeSpaces(r.job.name, 14)}${s"[${this.completeSpaces(r.links.toString, 4)} Links]"}").joinLines)
  private def completeSpaces(content: String, n: Int): String = content + (1 to n-content.length).map(_ => " ").join

}
