package org.maple
package commands.storymaple

import commands.MyCommand
import model.Drop
import services.StatsService

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, Request}
import org.maple.builders.EmbedBuilder
import org.maple.mappers.EmbedMapper
import org.maple.utils.IterableUtils.IterableImprovements

import java.text.DecimalFormat
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.math.BigDecimal.RoundingMode

class Stats extends MyCommand {
  override def aliases: Seq[String] = Seq("check", "checkme", "stats", "statistics")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {

    val statsService: StatsService = StatsService.getInstance
    val embedMapper: EmbedMapper = new EmbedMapper

    val decimalFormat: DecimalFormat = new DecimalFormat("#.##")

    val inputArg = arguments
      .join(" ")
      .trim
      .filter(_.isDigit)

    val user = msg.user
    val userId = Option(inputArg).filter(_.nonEmpty).getOrElse(msg.user.id.toString)
    val embed = msg
      .message
      .guild(msg.cache)
      .map(guild => {
        val userResult = statsService.findUser(userId, guild.id.toString)
          .map(usr => embedMapper
            .defaultEmbedBuilder("User Statistics", user)
            .withField("Boomer Stamps", s"${usr.boomerStampCount} <:boomerissa:1028876085976375296>")
            .withField("Total Kills", s"${usr.totalKills} :skull:")
            .withField("Sun Crystals", s"${usr.scCount} <:suncrystal:1026148453954371664>")
            .withField("SC Rate", s"${decimalFormat.format(usr.sunCrystalRate)}%")
//            .withField("Scrolls", s"${usr.scrollCount} <:10scroll:1026232443449126962>")
//            .withField("Scroll Rate", s"${decimalFormat.format(usr.scrollRate)}%")
            .withField("Donut Resets", s"${usr.donutCount} <:donut:1026233025236828180>")
            .withField("D Rate", s"${decimalFormat.format(usr.donutRate)}%")
            .withField("Last Lily", Option(usr.lastRoll).map(_.toString).getOrElse("Never"))
            .build
          )

          Await.result(userResult, Duration.Inf)
      })
      .getOrElse(embedMapper
        .toErrorEmbed("This is not a discord server!", "Lilynouch Hunt Count Error", user))

    CreateMessage(msg.textChannel.id, CreateMessageData(embeds = Seq(embed)))
  }

}
