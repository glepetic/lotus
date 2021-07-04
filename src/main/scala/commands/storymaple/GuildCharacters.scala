package org.maple
package commands.storymaple

import commands.MyCommand
import config.BotEnvironment
import services.RankingsService
import utils.IterableUtils._
import utils.discord.Markdown

import ackcord.commands.UserCommandMessage
import ackcord.requests.{DeleteMessage, Request}
import ackcord.syntax.TextChannelSyntax

class GuildCharacters extends MyCommand {
  override def name(): String = "guild"

  override def execute(msg: UserCommandMessage[_], arguments: List[String]): Request[_] = {
    val rankingsService = new RankingsService()

    val parsedGuildName = arguments.join.trim.toUpperCase
    val characters = rankingsService.getGuildCharacters(parsedGuildName)

    val content = characters match {
      case Nil => s"No members found for guild: ${parsedGuildName}!"
      case gc => s"Found ${Markdown.bold(gc.length.toString)} characters for guild ${Markdown.bold(parsedGuildName)}: ${Markdown.codeSnippet(gc.map(c => c.ign).joinWords)}"
    }

    BotEnvironment.client.foreach(client => client.requestsHelper.run(DeleteMessage(msg.textChannel.id, msg.message.id))(msg.cache))
    msg.textChannel.sendMessage(content)
  }
}
