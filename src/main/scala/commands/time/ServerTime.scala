package org.maple
package commands.time

import commands.MyCommand

import ackcord.commands.UserCommandMessage
import ackcord.requests.Request
import ackcord.syntax.TextChannelSyntax

class ServerTime extends MyCommand {
  override def aliases: Seq[String] = Seq("servertime", "st")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = arguments match {
    case Nil => new Now().execute(msg, List("CST/CDT"))
    case _ => msg.textChannel.sendMessage("Server time command does not take parameters!")
  }
}
