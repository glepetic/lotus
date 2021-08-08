package org.maple
package commands.storymaple.bossing

import builders.EmbedBuilder
import commands.MyCommand
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
      .withOptField("Party 1", Option(br.party1Raiders)
          .filter(_.nonEmpty)
        .map(pt1 => Markdown.codeSnippet(pt1.map(r => s"${this.completeSpaces(r.ign, 14)}${this.completeSpaces(r.level.toString, 5)}${this.completeSpaces(r.job.name, 14)}${this.completeSpaces(s"[${r.links} Links]",10)}").joinLines)))
      .withOptField("Party 2", Option(br.party2Raiders)
        .filter(_.nonEmpty)
        .map(pt2 => Markdown.codeSnippet(pt2.map(r => s"${this.completeSpaces(r.ign, 14)}${this.completeSpaces(r.level.toString, 5)}${this.completeSpaces(r.job.name, 14)}${this.completeSpaces(s"[${r.links} Links]",10)}").joinLines)))
      .withOptField("Fillers", Option(br.fillers)
        .filter(_.nonEmpty)
        .map(fillers => Markdown.codeSnippet(fillers.map(r => s"${this.completeSpaces(r.ign, 14)}${this.completeSpaces(r.level.toString, 5)}${this.completeSpaces(r.job.name, 14)}${this.completeSpaces(s"[${r.links} Links]",10)}").joinLines)))
      .withField("Jobs", br.jobs.map(j => s"${j.name} - ${Markdown.bold(j.count.toString)}").joinLines)
      .withField("Unique Jobs", br.uniqueJobsCount.toString, inline = true)
      .withField("Total Characters", br.totalCharacters.toString, inline = true)
      .build
    ).getOrElse(embedBuilder.defaultErrorColor.defaultErrorThumbnail.title("Boss Roster Error").description(s"Could not retrieve data for ${Markdown.bold(arguments.joinWords)}").build)

    CreateMessage(msg.textChannel.id, CreateMessageData(embed = Option(embed)))
  }

  private def completeSpaces(content: String, n: Int): String = content + (1 to n-content.length).map(_ => " ").join

}
