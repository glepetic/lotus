package org.maple
package commands.basic

import commands.MyCommand

import ackcord.commands.UserCommandMessage
import ackcord.requests.Request
import ackcord.syntax.TextChannelSyntax

class Ping extends MyCommand {
  override def name(): String = "ping"

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] =
    msg.textChannel.sendMessage("pong")
}
