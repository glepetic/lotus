package org.maple
package model

import java.time.temporal.ChronoUnit
import java.time.{Instant, LocalDate}
import java.util.UUID
import scala.math.BigDecimal.RoundingMode

case class SCUser(userId: String,
                  serverId: String,
                  lastRoll: LocalDate,
                  scCount: Long,
                  donutCount: Long,
                  scrollCount: Long,
                  id: String = UUID.randomUUID().toString) {

  def canDoLilynouch: Boolean = Option(lastRoll).forall(_.isBefore(LocalDate.now()))

  def totalKills: Long = scCount + donutCount + scrollCount

  def sunCrystalRate: BigDecimal = this.rate(scCount)
  def scrollRate: BigDecimal = this.rate(scrollCount)
  def donutRate: BigDecimal = this.rate(donutCount)

  private def rate(count: Long): BigDecimal = BigDecimal(Option(totalKills)
    .filter(tk => tk > 0)
    .map(tk => count*100.00/tk)
    .getOrElse(0))
    .setScale(2, RoundingMode.HALF_UP)

  def expectedSuncrystals: Long = totalKills/6
  def expectedScrolls: Long = (totalKills*247)/300
  def expectedDonuts: Long = totalKills/100

  def suncrystalOffset: Double = ((scCount-expectedSuncrystals)*100.00)/expectedSuncrystals
  def scrollsOffset: Double =  ((scCount-expectedScrolls)*100.00)/expectedScrolls
  def donutsOffset: Double =  ((donutCount-expectedDonuts)*100.00)/expectedDonuts

  def increaseSCNoTimeUpdate: SCUser = SCUser(userId, serverId, lastRoll, scCount + 1, donutCount, scrollCount, id)
  def increaseSC: SCUser = SCUser(userId, serverId, lastRoll = LocalDate.now(), scCount + 1, donutCount, scrollCount, id)
  def increaseDonut: SCUser = SCUser(userId, serverId, lastRoll = LocalDate.now(), scCount, donutCount + 1, scrollCount, id)
  def increaseScroll: SCUser = SCUser(userId, serverId, lastRoll = LocalDate.now(), scCount, donutCount, scrollCount + 1, id)

}
