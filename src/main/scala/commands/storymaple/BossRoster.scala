package org.maple
package commands.storymaple

import builders.EmbedBuilder
import commands.MyCommand
import config.BotEnvironment
import services.{BossRunsService, RankingsService}
import utils.IterableUtils._
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, DeleteMessage, Request}

class BossRoster extends MyCommand {
  override def aliases: Seq[String] = Seq("br","bossroster")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val bossRunsService = new BossRunsService()

    val bossRoster = Option(bossRunsService.getRoster(arguments))
      .filter(roster => roster.raiders.nonEmpty)

    val embedBuilder = EmbedBuilder
      .builder
      .authorRequestedBy(msg.user)
      .defaultFooter

    val embed = bossRoster.map(br => embedBuilder
      .defaultColor
      .defaultThumbnail
      .title("Boss Roster")
      .description(br.raiders.map(r => s"${Markdown.bold(r.ign)} - Lvl. ${r.level} - ${Markdown.bold(r.job.name)} [${r.links} Links]").joinLines)
      .withField("Jobs", br.jobs.map(j => s"${j.name} - ${Markdown.bold(j.count.toString)}").joinLines)
      .withField("Unique Jobs", br.uniqueJobsCount.toString, inline = true)
      .withField("Total Characters", br.totalCharacters.toString, inline = true)
      .build
    ).getOrElse(embedBuilder.defaultErrorColor.defaultErrorThumbnail.title("Boss Roster Error").description(s"Could not retrieve data for ${Markdown.bold(arguments.joinWords)}").build)

    CreateMessage(msg.textChannel.id, CreateMessageData(embed = Option(embed)))
  }
}
