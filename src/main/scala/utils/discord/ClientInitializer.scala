package org.maple
package utils.discord

import listeners.{CommandListener, EventListener}

import ackcord.DiscordClient

object ClientInitializer {

  def initialize(client: DiscordClient): Unit = {
    this.addEventListener(client)
    this.addCommandListener(client)
  }

  private def addEventListener(client: DiscordClient) = {
    val myListeners = new EventListener(client.requests)
    client.registerListener(myListeners.onReady)
    client.registerListener(myListeners.onReaction)
    client.registerListener(myListeners.onMessageDelete)
    client.registerListener(myListeners.onMessageSent)
  }

  private def addCommandListener(client: DiscordClient): Unit = {
    val commands = new CommandListener(client.requests)
    commands.enabledNamedCommands.foreach(c => client.commands.runNewNamedCommand(c))
  }

}
