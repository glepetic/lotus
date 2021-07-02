package org.maple
package commands.basic

import commands.MyCommand
import repositories.MemeRepository

import ackcord.commands.UserCommandMessage
import ackcord.requests.Request
import ackcord.syntax.TextChannelSyntax

class Meme extends MyCommand {
  override def name(): String = "meme"
  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = MemeRepository.getInstance
    .getMaplestoryMeme
    .map(meme => msg.textChannel.sendMessage(meme))
    .getOrElse(msg.textChannel.sendMessage("Do I look like a clown to you?"))
}
