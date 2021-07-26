package org.maple
package dto

import model.maplestory.Jobs

case class BossRosterDto(raiders: List[RaiderDto]) {

  def jobs: List[JobDto] = Jobs.branches
    .map(branch => JobDto(branch, raiders.count(r => r.job.branch equals branch)))
    .filter(job => job.count > 0)
    .sortBy(-_.count)

  def uniqueJobsCount: Int = this.jobs.length
  def totalCharacters: Int = this.raiders.length

}
