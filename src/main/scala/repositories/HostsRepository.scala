package org.maple
package repositories

import model.maplestory.BossRun

class HostsRepository {

  var bossRuns: List[BossRun] = Nil

  def insert(br: BossRun): Unit = this.bossRuns = this.bossRuns ++ List(br)
  def find(hostId: String, channelId: String): Option[BossRun] =
    this.bossRuns.find(br => (br.hostId equalsIgnoreCase hostId) && (br.channelId equalsIgnoreCase channelId))
  def find(messageId: String): Option[BossRun] = this.bossRuns.find(br => br.messageId equalsIgnoreCase messageId)
  def update(br: BossRun): Unit = this.bossRuns = this.bossRuns.filterNot(bossRun => bossRun.messageId equalsIgnoreCase br.messageId) ++ List(br)

}

object HostsRepository {
  private val instance = new HostsRepository
  def getInstance: HostsRepository = instance
}
