package org.maple
package config

import utils.OptionUtils._
import BotEnvironment.getStringProperty

object HttpClientConfig {

  val storymapleWebsiteHost: String = getStringProperty("storymaple.host").orThrow
  val rankingsUrl: String = getStringProperty("storymaple.rankings.search.url").orThrow

  val worldTimeApiHost: String = getStringProperty("worldtime.api.host").orThrow
  val worldTimeApiTimezonesUrl: String = getStringProperty("worldtime.api.timezones.url").orThrow

}
