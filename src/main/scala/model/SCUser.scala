package org.maple
package model

import java.time.temporal.ChronoUnit
import java.time.{Instant, ZoneId}
import java.util.UUID

case class SCUser(userId: String,
                  serverId: String,
                  lastRoll: Instant,
                  scCount: Int,
                  donutCount: Int,
                  scrollCount: Int,
                  id: String = UUID.randomUUID().toString) {

  def canDoLilynouch: Boolean = {
    val lastRolled = Option(lastRoll).map(_.atZone(ZoneId.systemDefault()))
    val now = Instant.now().atZone(ZoneId.systemDefault())
    val result = lastRolled.forall(lR => (lR.getDayOfMonth < now.getDayOfMonth || ChronoUnit.DAYS.between(lR, now) > 1) && lR.isBefore(now))
    result
  }

  def scIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount + 1, donutCount, scrollCount, id)
  def donutIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount, donutCount + 1, scrollCount, id)
  def scrollIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount, donutCount, scrollCount + 1, id)

}
