package org.maple
package commands.storymaple

import commands.MyCommand
import model.Drop
import services.SCService

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, CreateReaction, Request}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class Lilynouch extends MyCommand {
  override def aliases: Seq[String] = Seq("lily")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {

    val scService: SCService = SCService.getInstance

    val userId = msg.user.id.toString
    val result = msg
      .message
      .guild(msg.cache)
      .map(guild => {
        val lilyFightResult = scService.fightLilynouch(userId, guild.id.toString)
          .map {
            case Drop.DONUT => "<:donut:1026233025236828180>"
            case Drop.SUNCRYSTAL => "<:suncrystal:1026148453954371664>"
            case Drop.SCROLL => "<:10scroll:1026232443449126962>"
          }
          .fallbackTo(Future("You have already done Lily recently!"))

          Await.result(lilyFightResult, Duration.Inf)
      })
      .getOrElse("This is not a discord server!")



    CreateMessage(msg.textChannel.id, CreateMessageData(result))

  }

}
