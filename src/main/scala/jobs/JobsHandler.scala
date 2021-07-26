package org.maple
package jobs

object JobsHandler {

  private var jobs: List[MyJob] = Nil

  def hasRoyaleJob: Boolean = this.jobs.exists(job => job.isInstanceOf[RoyaleNotification])
  def addJob(job: MyJob): Unit = jobs = job::jobs

}
