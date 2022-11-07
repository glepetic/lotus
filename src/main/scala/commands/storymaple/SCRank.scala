package org.maple
package commands.storymaple

import commands.MyCommand
import mappers.EmbedMapper
import services.StatsService
import utils.IterableUtils.IterableImprovements

import ackcord.commands.UserCommandMessage
import ackcord.data.UserId
import ackcord.requests.{CreateMessage, CreateMessageData, GetUser, Request}
import org.maple.config.BotEnvironment
import org.maple.dto.SCUserDto

import java.text.DecimalFormat
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class SCRank extends MyCommand {
  override def aliases: Seq[String] = Seq("scrank", "scr")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {

    val statsService: StatsService = StatsService.getInstance
    val embedMapper: EmbedMapper = new EmbedMapper

    val user = msg.user

    val embedFuture = msg.message
      .guild(msg.cache)
      .map(guild => statsService.findTopUsers10BySCCount(guild.id.toString)
        .map(users => users.map(usr =>
          BotEnvironment.client.flatMap(c => Await.result(c.requestsHelper.run(GetUser(UserId(usr.userId)))(msg.cache).value, Duration.Inf))
            .map(discordUsr => SCUserDto(s"${discordUsr.username}#${discordUsr.discriminator}", usr.scCount))
        )
          .filter(_.nonEmpty)
          .map(_.get)
          .map(usrDto => s"${usrDto.userIdentifier}: ${usrDto.scCount}")
          .joinLines
        )
        .map(table => embedMapper
          .defaultEmbedBuilder("Sun Crystal Rankings", user)
          .withField("Top 10", table)
          .build
        )
      )
      .getOrElse(Future(embedMapper.toErrorEmbed("Unable to retrieve ranking from database", "Suncrystal Ranking Error", user)))

    val embed = Await.result(embedFuture, Duration.Inf)

    CreateMessage(msg.textChannel.id, CreateMessageData(embeds = Seq(embed)))
  }

}
