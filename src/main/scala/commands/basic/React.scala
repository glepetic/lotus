package org.maple
package commands.basic

import commands.MyCommand

import ackcord.commands.UserCommandMessage
import ackcord.requests.{CreateReaction, Request}

class React extends MyCommand{
  override def name(): String = "react"

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] =
    CreateReaction(msg.textChannel.id, msg.message.id, "\uD83C\uDF08")
}
