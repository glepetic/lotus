package org.maple
package commands.storymaple

import commands.MyCommand
import services.TrollService

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateMessage, CreateMessageData, Request}

class RissaDK extends MyCommand {
  override def aliases: Seq[String] = Seq("rdk","rissadk")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {

    val trollService: TrollService = TrollService.getInstance

    CreateMessage(msg.textChannel.id, CreateMessageData(trollService.getRissaIGNDK))
  }

}
