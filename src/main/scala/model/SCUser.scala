package org.maple
package model

import java.time.{Instant, LocalDateTime, ZoneId}
import java.util.UUID

case class SCUser(userId: String,
                  serverId: String,
                  lastRoll: Instant,
                  scCount: Int,
                  donutCount: Int,
                  scrollCount: Int,
                  id: String = UUID.randomUUID().toString) {

  def canDoLilynouch: Boolean = Option(lastRoll)
    .forall(lR => lR.atZone(ZoneId.of("UTC")).getDayOfMonth < LocalDateTime.now().getDayOfMonth)

  def scIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount + 1, donutCount, scrollCount, id)
  def donutIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount, donutCount + 1, scrollCount, id)
  def scrollIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount, donutCount, scrollCount + 1, id)

}
