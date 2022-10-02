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

    scService.fightLilynouch(msg.user.id.toString, msg.message.guild(msg.cache).orThrow.id.toString)
      .map {
        case Drop.DONUT => "<:donut:1026233067385397248>"
        case Drop.SUNCRYSTAL => "<:suncrystal:1026233773832032331>"
        case Drop.SCROLL => "<:10scroll:1026233940547207278>"
      }
      .foreach(emoji => {
        BotEnvironment.client.foreach(client => client.requestsHelper.run(CreateMessage(msg.textChannel.id, CreateMessageData(emoji)))(msg.cache))
      })

    CreateReaction(msg.message.channelId, msg.message.id, "greencheck:871199809493671978")
  }

}
