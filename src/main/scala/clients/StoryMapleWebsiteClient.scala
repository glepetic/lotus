package org.maple
package clients

import config.HttpClientConfig

import spray.json._

class StoryMapleWebsiteClient extends MyClient {

  override protected def host: String = HttpClientConfig.storymapleWebsiteHost
  override protected def timeout: Int = HttpClientConfig.storyMapleTimeout

  private val rankings: String = HttpClientConfig.rankingsUrl
  private val drops: String = HttpClientConfig.dropsUrl

  def getRankings: Option[JsValue] = this.get(this.rankings)
  def getMobDrops: Option[JsValue] = this.get(this.drops)


}
