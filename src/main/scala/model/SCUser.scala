package org.maple
package model

import java.time.temporal.ChronoUnit
import java.time.{Instant, ZoneId}
import java.util.UUID
import scala.math.BigDecimal.RoundingMode

case class SCUser(userId: String,
                  serverId: String,
                  lastRoll: Instant,
                  scCount: Long,
                  donutCount: Long,
                  scrollCount: Long,
                  id: String = UUID.randomUUID().toString) {

  def canDoLilynouch: Boolean = {
    val lastRolled = Option(lastRoll).map(_.atZone(ZoneId.systemDefault()))
    val now = Instant.now().atZone(ZoneId.systemDefault())
    val result = lastRolled.forall(lR => (lR.getDayOfMonth < now.getDayOfMonth || ChronoUnit.DAYS.between(lR, now) > 1) && lR.isBefore(now))
    result
  }

  def totalKills: Long = scCount + donutCount + scrollCount

  def sunCrystalRate: BigDecimal = BigDecimal(scCount*100.00/totalKills).setScale(2, RoundingMode.HALF_UP)
  def scrollRate: BigDecimal = BigDecimal(scrollCount*100.00/totalKills).setScale(2, RoundingMode.HALF_UP)
  def donutRate: BigDecimal = BigDecimal(donutCount*100.00/totalKills).setScale(2, RoundingMode.HALF_UP)

  private def expectedSuncrystals: Double = totalKills/6
  private def expectedScrolls: Double = (totalKills*247)/300
  private def expectedDonuts: Double = totalKills/100

  def suncrystalOffset: Double = ((scCount-expectedSuncrystals)*100.00)/expectedSuncrystals
  def scrollsOffset: Double =  ((scCount-expectedScrolls)*100.00)/expectedScrolls
  def donutsOffset: Double =  ((donutCount-expectedDonuts)*100.00)/expectedDonuts

  def scIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount + 1, donutCount, scrollCount, id)
  def donutIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount, donutCount + 1, scrollCount, id)
  def scrollIncreaser: SCUser = SCUser(userId, serverId, lastRoll = Instant.now(), scCount, donutCount, scrollCount + 1, id)

}
