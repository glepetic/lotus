package org.maple
package dto

import model.maplestory.Jobs

case class BossRosterDto(party1Raiders: List[RaiderDto], party2Raiders: List[RaiderDto], fillers: List[RaiderDto]) {

  def hasRaiders: Boolean = party1Raiders.nonEmpty || party2Raiders.nonEmpty

  def jobs: List[JobDto] = Jobs.branches
    .map(branch => JobDto(branch, (party1Raiders ++ party2Raiders).count(r => r.job.branch equals branch)))
    .filter(job => job.count > 0)
    .sortBy(-_.count)

  def uniqueJobsCount: Int = this.jobs.length
  def totalCharacters: Int = this.party1Raiders.length + this.party2Raiders.length + this.fillers.length

}
