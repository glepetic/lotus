package org.maple
package builders

import utils.discord.{AvatarURLGenerator, Colors}

import ackcord.data.{EmbedField, OutgoingEmbed, OutgoingEmbedAuthor, OutgoingEmbedFooter, OutgoingEmbedImage, OutgoingEmbedThumbnail, OutgoingEmbedVideo, User}
import org.maple.config.{BotEnvironment, MessageProperties}

import java.time.OffsetDateTime

class EmbedBuilder(private val title: Option[String],
                   private val description: Option[String],
                   private val url: Option[String],
                   private val timestamp: Option[OffsetDateTime],
                   private val color: Option[Int],
                   private val footer: Option[OutgoingEmbedFooter],
                   private val image: Option[OutgoingEmbedImage],
                   private val video: Option[OutgoingEmbedVideo],
                   private val thumbnail: Option[OutgoingEmbedThumbnail],
                   private val author: Option[OutgoingEmbedAuthor],
                   private val fields: Seq[EmbedField]) {

  def title(t: String): EmbedBuilder = new EmbedBuilder(Option(t), description, url, timestamp, color, footer, image, video, thumbnail, author, fields)
  def description(d: String): EmbedBuilder = new EmbedBuilder(title, Option(d), url, timestamp, color, footer, image, video, thumbnail, author, fields)
  def url(u: String): EmbedBuilder = new EmbedBuilder(title, description, Option(u), timestamp, color, footer, image, video, thumbnail, author, fields)
  def timestamp(t: OffsetDateTime): EmbedBuilder = new EmbedBuilder(title, description, url, Option(t), color, footer, image, video, thumbnail, author, fields)
  def defaultColor: EmbedBuilder = this.color("yellow")
  def defaultErrorColor: EmbedBuilder = this.color("red")
  def color(col: String): EmbedBuilder = new EmbedBuilder(title, description, url, timestamp, Option(col).map(c => Colors.get(c)), footer, image, video, thumbnail, author, fields)
  def defaultFooter: EmbedBuilder = this.footer(s"Please report bugs or make suggestions to ${BotEnvironment.botOwnerUsername}", None)
  def footer(text: String, icon: Option[String]): EmbedBuilder = new EmbedBuilder(title, description, url, timestamp, color, Option(OutgoingEmbedFooter(text,icon)), image, video, thumbnail, author, fields)
  def defaultImage: EmbedBuilder = this.image(BotEnvironment.publicPath + "/mushroom.png")
  def image(i: String): EmbedBuilder = new EmbedBuilder(title, description, url, timestamp, color, footer, Option(OutgoingEmbedImage(i)), video, thumbnail, author, fields)
  def video(v: String): EmbedBuilder = new EmbedBuilder(title, description, url, timestamp, color, footer, image, Option(OutgoingEmbedVideo(v)), thumbnail, author, fields)
  def defaultThumbnail: EmbedBuilder = this.thumbnail(MessageProperties.defaultThumbnail)
  def defaultErrorThumbnail: EmbedBuilder = this.thumbnail(MessageProperties.defaultErrorThumbnail)
  def thumbnail(thumbnail: Option[String]): EmbedBuilder = new EmbedBuilder(title, description, url, timestamp, color, footer, image, video, thumbnail.map(t => OutgoingEmbedThumbnail(t)), author, fields)
  def authorRequestedBy(u: User): EmbedBuilder = this.author(s"Requested by ${u.username}#${u.discriminator}", None, AvatarURLGenerator.generate(Option(u)))
  def author(name: String, icon: Option[String]): EmbedBuilder = this.author(name,None,icon)
  def author(name: String, u: Option[String], icon: Option[String]): EmbedBuilder = new EmbedBuilder(title, description, url, timestamp, color, footer, image, video, thumbnail, Option(OutgoingEmbedAuthor(name,u,icon)), fields)
  def fields(f: Seq[EmbedField]): EmbedBuilder = new EmbedBuilder(title, description, url, timestamp, color, footer, image, video, thumbnail, author, f)
  def withField(name: String, content: String): EmbedBuilder =  this.withField(name,content,inline = false)
  def withField(name: String, content: String, inline: Boolean): EmbedBuilder = new EmbedBuilder(title, description, url, timestamp, color, footer, image, video, thumbnail, author, fields ++ Seq(EmbedField(name, content, Option(inline))))

  def build: OutgoingEmbed = OutgoingEmbed(title, description, url, timestamp, color, footer, image, video, thumbnail, author, fields)

}

object EmbedBuilder {
  def builder : EmbedBuilder = new EmbedBuilder(None, None, None, None, None, None, None, None, None, None, Nil)
}
