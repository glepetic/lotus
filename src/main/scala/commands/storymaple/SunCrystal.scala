package org.maple
package commands.storymaple

import commands.MyCommand
import model.Drop
import services.SCService

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, Request}
import org.maple.builders.EmbedBuilder
import org.maple.mappers.EmbedMapper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class SunCrystal extends MyCommand {
  override def aliases: Seq[String] = Seq("sc", "suncrystal")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {

    val scService: SCService = SCService.getInstance
    val embedMapper: EmbedMapper = new EmbedMapper

    val user = msg.user
    val userId = msg.user.id.toString
    val embed = msg
      .message
      .guild(msg.cache)
      .map(guild => {
        val lilyFightResult = scService.findScUser(userId, guild.id.toString)
          .map(scUser => embedMapper
            .defaultEmbedBuilder("Lilynouch Hunt Count", user)
            .withField("Total Kills", s"${scUser.totalKills} :skull:")
            .withField("Sun Crystals", s"${scUser.scCount} <:suncrystal:1026148453954371664>")
            .withField("Rate", s"${scUser.scCount*100.00/scUser.totalKills}%", inline = true)
            .withField("Scrolls", s"${scUser.scrollCount} <:10scroll:1026232443449126962>")
            .withField("Rate", s"${scUser.scrollCount*100.00/scUser.totalKills}%", inline = true)
            .withField("Donut Resets", s"${scUser.donutCount} <:donut:1026233025236828180>")
            .withField("Rate", s"${scUser.donutCount*100.00/scUser.totalKills}%", inline = true)
            .build
          )
          .fallbackTo(Future(
            embedMapper
              .toErrorEmbed("Could not complete the request", "Lilynouch Hunt Count Error", user)
          ))

          Await.result(lilyFightResult, Duration.Inf)
      })
      .getOrElse(embedMapper
        .toErrorEmbed("This is not a discord server!", "Lilynouch Hunt Count Error", user))

    CreateMessage(msg.textChannel.id, CreateMessageData(embeds = Seq(embed)))
  }

}
