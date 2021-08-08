package org.maple
package services

import dto.BossRosterDto
import mappers.BossRosterMapper

import scala.collection.immutable.ListSet

class BossRunsService {

  val rankingsService: RankingsService = RankingsService.getInstance
  val bossRosterMapper = new BossRosterMapper()

  def getRoster(igns: List[String]): BossRosterDto = {
    val rankingPlayers = this.rankingsService.getRankings
    val raiders = ListSet.from(igns)
      .map(ign => rankingPlayers.find(player => player.hasCharacterWithIgn(ign)))
      .map(playerOpt => playerOpt.flatMap(player => this.bossRosterMapper.toRaider(player, igns)))
      .filter(_.nonEmpty)
      .map(_.get)
      .toList

    //    val raiders = this.rankingsService.getRankings
    //      .filter(player => player.characters.exists(c => igns.map(_.toLowerCase) contains c.ign.toLowerCase))
    //      .map(player => this.bossRosterMapper.toRaider(player, igns))
    //      .filter(_.nonEmpty)
    //      .map(_.get)
    this.bossRosterMapper.toBossRoster(raiders)
  }
}


object BossRunsService {
  private val instance: BossRunsService = new BossRunsService()

  def getInstance: BossRunsService = instance
}




