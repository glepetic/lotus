package org.maple
package commands.storymaple

import commands.MyCommand
import services.SCService

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, CreateReaction, DeleteMessage, Request}
import org.maple.config.BotEnvironment
import org.maple.model.Drop
import org.maple.utils.OptionUtils.OptionImprovements

import scala.concurrent.ExecutionContext.Implicits.global

class Lilynouch extends MyCommand {
  override def aliases: Seq[String] = Seq("lily")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {

    val scService: SCService = SCService.getInstance

    val message = msg.message
    val userId = msg.user.id.toString

    println("init command")

    scService.fightLilynouch(userId)
      .map {
        case Drop.DONUT => "<:donut:1026233025236828180>"
        case Drop.SUNCRYSTAL => "<:suncrystal:1026148453954371664>"
        case Drop.SCROLL => "<:10scroll:1026232443449126962>"
      }
      .foreach(emoji => {
        BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateMessage(msg.textChannel.id, CreateMessageData(emoji)))(msg.cache))
      })

    CreateReaction(message.channelId, message.id, "greencheck:871199809493671978")
  }

}
