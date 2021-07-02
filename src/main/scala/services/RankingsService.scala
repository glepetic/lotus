package org.maple
package services

import clients.StoryMapleWebsiteClient
import model.Player
import parsers.StoryMapleRankingsParser

class RankingsService {

  val client = new StoryMapleWebsiteClient()
  val parser = new StoryMapleRankingsParser()

  def getRankings: List[Player] = client.getRankings.map(rjson => this.parser.parse(rjson)).getOrElse(Nil)

  def getPlayer(ign: String): Option[Player] = this.getRankings.find(p => p.characters.map(c => c.ign.toLowerCase).contains(ign.toLowerCase))
  def getPlayer(rank: Int): Option[Player] = this.getRankings.find(p => p.rank == rank)

}

object RankingsService {
  private val instance: RankingsService = new RankingsService()

  def getInstance: RankingsService = instance
}
