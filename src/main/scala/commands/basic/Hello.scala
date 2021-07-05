package org.maple
package commands.basic

import commands.MyCommand

import ackcord.commands.UserCommandMessage
import ackcord.requests.Request
import ackcord.syntax.TextChannelSyntax

class Hello extends MyCommand {
  override def aliases: Seq[String] = Seq("hello")

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] =
    msg.textChannel.sendMessage(s"Hello ${msg.user.username}")
}
