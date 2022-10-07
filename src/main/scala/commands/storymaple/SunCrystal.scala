package org.maple
package commands.storymaple

import commands.MyCommand
import model.Drop
import services.SCService

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

class SunCrystal extends MyCommand {
  override def aliases: Seq[String] = Seq("sc", "suncrystal")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {

    val scService: SCService = SCService.getInstance
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
        val scUserResult = scService.findScUser(userId, guild.id.toString)
          .map(scUser => embedMapper
            .defaultEmbedBuilder("Lilynouch Hunt Count", user)
            .withField("Total Kills", s"${scUser.totalKills} :skull:")
            .withField("Sun Crystals", s"${scUser.scCount} <:suncrystal:1026148453954371664>", inline = true)
            .withField("Expected", s"${scUser.expectedSuncrystals}", inline = true)
            .withField("Rate", s"${decimalFormat.format(scUser.sunCrystalRate)}%", inline = true)
//            .withField("Offset", s"${decimalFormat.format(scUser.suncrystalOffset)}%", inline = true)
            .withField("Scrolls", s"${scUser.scrollCount} <:10scroll:1026232443449126962>", inline = true)
            .withField("Expected", s"${scUser.expectedScrolls}", inline = true)
            .withField("Rate", s"${decimalFormat.format(scUser.scrollRate)}%", inline = true)
//            .withField("Offset", s"${decimalFormat.format(scUser.scrollsOffset)}%", inline = true)
            .withField("Donut Resets", s"${scUser.donutCount} <:donut:1026233025236828180>", inline = true)
            .withField("Expected", s"${scUser.expectedDonuts}", inline = true)
            .withField("Rate", s"${decimalFormat.format(scUser.donutRate)}%", inline = true)
//            .withField("Offset", s"${decimalFormat.format(scUser.donutsOffset)}%", inline = true)
            .withField("Last Roll", Option(scUser.lastRoll).map(_.toString).getOrElse("Never"))
            .build
          )

          Await.result(scUserResult, Duration.Inf)
      })
      .getOrElse(embedMapper
        .toErrorEmbed("This is not a discord server!", "Lilynouch Hunt Count Error", user))

    CreateMessage(msg.textChannel.id, CreateMessageData(embeds = Seq(embed)))
  }

}
