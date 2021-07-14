package org.maple
package config

import utils.OptionUtils._

import BotEnvironment.{getIntProperty, getStringProperty}

object HttpClientConfig {

  val storymapleWebsiteHost: String = getStringProperty("storymaple.host").orThrow
  val rankingsUrl: String = getStringProperty("storymaple.rankings.search.url").orThrow
  val storyMapleTimeout: Int = getIntProperty("storymaple.timeout").orThrow

  val worldTimeApiHost: String = getStringProperty("worldtime.api.host").orThrow
  val worldTimeApiTimezonesUrl: String = getStringProperty("worldtime.api.timezones.url").orThrow

}
