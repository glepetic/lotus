package org.maple
package model

import java.time.temporal.ChronoUnit
import java.time.{Instant, ZoneId}
import java.util.UUID
import scala.math.BigDecimal.RoundingMode

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

  def totalKills: Int = scCount + donutCount + scrollCount

  def sunCrystalRate: BigDecimal = BigDecimal(scCount*100.00/totalKills).setScale(2, RoundingMode.HALF_UP)
  def scrollRate: BigDecimal = BigDecimal(scrollCount*100.00/totalKills).setScale(2, RoundingMode.HALF_UP)
  def donutRate: BigDecimal = BigDecimal(donutCount*100.00/totalKills).setScale(2, RoundingMode.HALF_UP)

  private def expectedSuncrystals: Int = totalKills/6
  private def expectedScrolls: Int = totalKills*247/300
  private def expectedDonuts: Int = totalKills/100

  def suncrystalOffset: BigDecimal = BigDecimal((100 - (scCount*100.00)/expectedSuncrystals) * -1).setScale(2, RoundingMode.FLOOR)
  def scrollsOffset: BigDecimal = BigDecimal((100 - (scrollCount*100.00)/expectedScrolls) * -1).setScale(2, RoundingMode.FLOOR)
  def donutsOffset: BigDecimal = BigDecimal((100 - (donutCount*100.00)/expectedDonuts) * -1).setScale(2, RoundingMode.FLOOR)

  def scIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount + 1, donutCount, scrollCount, id)
  def donutIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount, donutCount + 1, scrollCount, id)
  def scrollIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount, donutCount, scrollCount + 1, id)

}
