package org.maple
package mappers

import builders.EmbedBuilder
import model.maplestory.Player
import utils.IterableUtils.IterableImprovements
import utils.discord.Markdown

import ackcord.data.{OutgoingEmbed, User}

class EmbedMapper {

  def defaultEmbedBuilder(title: String, author: User): EmbedBuilder = EmbedBuilder
    .builder
    .title(title)
    .authorRequestedBy(author)
    .defaultFooter

  def toEmbed(player: Player, title: String, author: User): OutgoingEmbed = this.defaultEmbedBuilder(title, author)
    .defaultColor
    .defaultThumbnail
    .description(player.characters.map(c => s"${Markdown.bold(c.ign)} - Lvl. ${c.level} - ${Markdown.bold(c.job.name)}${Option(c.guild).map(g => " - " + g).getOrElse("")}").joinLines)
    .withField("Rank", player.rank.toString, inline = true)
    .withField("Link Levels", player.linkLevels.toString, inline = true)
    .withField("Nirvana", player.nirvana.toString, inline = true)
    .build

  def toErrorEmbed(description: String, title: String, author: User): OutgoingEmbed = this.defaultEmbedBuilder(title, author)
    .defaultErrorColor
    .defaultErrorThumbnail
    .description(description)
    .build

}
