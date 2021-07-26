package org.maple
package services

import dto.BossRosterDto
import mappers.BossRosterMapper

class BossRunsService {

  val rankingsService: RankingsService = RankingsService.getInstance
  val bossRosterMapper = new BossRosterMapper()

  def getRoster(igns: Seq[String]): BossRosterDto = {
    val y = this.rankingsService.getRankings
    val x = y
      .filter(player => player.characters.exists(c => igns.map(_.toLowerCase) contains c.ign.toLowerCase))
    val raiders = x
      .map(player => this.bossRosterMapper.toRaider(player, igns))
      .filter(_.nonEmpty)
      .map(_.get)
    this.bossRosterMapper.toBossRoster(raiders)
  }
}


object BossRunsService {
  private val instance: BossRunsService = new BossRunsService()

  def getInstance: BossRunsService = instance
}




