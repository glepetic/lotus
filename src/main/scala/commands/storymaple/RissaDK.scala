package org.maple
package commands.storymaple

import commands.MyCommand
import services.TrollService

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, Request}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class RissaDK extends MyCommand {
  override def aliases: Seq[String] = Seq("rdk", "rissadk")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {

    val trollService: TrollService = TrollService.getInstance

    val result = msg
      .message
      .guild(msg.cache)
      .map(guild => Await.result(trollService.getRissaIGNDK(msg.user.id.toString, guild.id.toString), Duration.Inf))
      .getOrElse("This is not a discord server!")

    CreateMessage(msg.textChannel.id, CreateMessageData(result))
  }

}
