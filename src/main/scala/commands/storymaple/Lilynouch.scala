package org.maple
package commands.storymaple

import commands.MyCommand
import model.Drop
import services.StatsService

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, Request}

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.{HOURS, MINUTES, SECONDS}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class Lilynouch extends MyCommand {
  override def aliases: Seq[String] = Seq("lily")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {

    val statsService: StatsService = StatsService.getInstance

    val user = msg.user
    val userId = user.id.toString
    val result = msg
      .message
      .guild(msg.cache)
      .map(guild => {
        val lilyFightResult = statsService.fightLilynouch(userId, guild.id.toString)
          .map {
            case Drop.DONUT => s"${user.mentionNick} A :doughnut: man pops up." +
              "\"*We* have proof that you have been harrassing deafbat at Target Dummies. *We* do not take harrassment lightly. I will have to confiscate 3 of your sun crystals.\"\n" +
              "Fucking donut... <:donut:1026233025236828180>"
            case Drop.SUNCRYSTAL => s"${user.mentionNick} <:suncrystal:1026148453954371664> I have received a crafting material from Lilynouch!"
            case Drop.SCROLL => s"${user.mentionNick} <:10scroll:1026232443449126962> Try harder bitch :)"
          }
          .fallbackTo(this.lilyOnCooldown)

        Await.result(lilyFightResult, Duration.Inf)
      })
      .getOrElse("This is not a discord server!")


    CreateMessage(msg.textChannel.id, CreateMessageData(result))

  }

  private def lilyOnCooldown: Future[String] = {
    val now = LocalDateTime.now();
    val reset = now
      .plusDays(1)
      .withHour(0)
      .withMinute(0)
      .withSecond(0);
    val hours = HOURS.between(now, reset)
    val minutes = MINUTES.between(now, reset) - hours * 60
    val seconds = SECONDS.between(now, reset) - hours * 60 * 60 - minutes * 60
    Future("You already did Lily today bitch <:gunsmile:1026528430277263423>\n" +
      s"Wait $hours hours $minutes minutes and $seconds seconds, then try again"
    )
  }

}
