package org.maple
package commands.storymaple

import commands.MyCommand
import services.SCService

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, DeleteMessage, Request}
import org.maple.config.BotEnvironment
import org.maple.utils.OptionUtils.OptionImprovements

import scala.concurrent.ExecutionContext.Implicits.global

class Lilynouch extends MyCommand {
  override def aliases: Seq[String] = Seq("lily")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {

    val scService: SCService = SCService.getInstance

    scService.fightLilynouch(msg.user.id.toString, msg.message.guild(msg.cache).orThrow.id.toString)
      .foreach(dropType => {
        BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateMessage(msg.textChannel.id, CreateMessageData(dropType.value)))(msg.cache))
      })

    DeleteMessage(msg.textChannel.id, msg.message.id)
  }

}
