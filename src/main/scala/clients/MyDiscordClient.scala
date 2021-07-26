package org.maple
package clients

import config.BotEnvironment
import utils.OptionUtils.OptionImprovements

import ackcord.data.raw.{RawChannel, RawGuild}
import ackcord.data.{GuildId, Message}
import ackcord.requests.{DeleteMessage, GetGuild, GetGuildChannels, Request}
import ackcord.{CacheSnapshot, DiscordClient, OptFuture}

case class MyDiscordClient(private val client: DiscordClient, private val cache: CacheSnapshot) {

  def run[A](request: Request[A]): OptFuture[A] = this.client.requestsHelper.run(request)(this.cache)

  def getGuildChannels(id: GuildId): OptFuture[Seq[RawChannel]] = client.requestsHelper.run(GetGuildChannels(id))(cache)
  def getGuild(id: GuildId): OptFuture[RawGuild] = client.requestsHelper.run(GetGuild(id))(cache)
  def deleteMessage(msg: Message): Unit = client.requestsHelper.run(DeleteMessage(msg.channelId, msg.id))(cache)

}

object MyDiscordClient {
  def withCache(cache: CacheSnapshot): MyDiscordClient = new MyDiscordClient(BotEnvironment.client.orThrow, cache)
}
