package org.maple
package model

import java.time.LocalDate
import java.util.UUID
import scala.math.BigDecimal.RoundingMode

case class User(userId: String,
                serverId: String,
                lastRoll: LocalDate,
                scCount: Long,
                donutCount: Long,
                scrollCount: Long,
                boomerStampCount: Long,
                id: String = UUID.randomUUID().toString) {

  def canDoLilynouch: Boolean = Option(lastRoll).forall(_.isBefore(LocalDate.now()))

//  def totalKills: Long = scCount + donutCount*4 + scrollCount
  def totalKills: Long = 420

//  def sunCrystalRate: BigDecimal = this.rate(scCount)
  def sunCrystalRate: BigDecimal = 69
//  def scrollRate: BigDecimal = this.rate(scrollCount)
  def scrollRate: BigDecimal = 69
//  def donutRate: BigDecimal = this.rate(donutCount)
  def donutRate: BigDecimal = 69

  private def rate(count: Long): BigDecimal = Option(totalKills)
    .filter(tk => tk > 0)
    .map(tk => BigDecimal(count*100.00/tk).setScale(2, RoundingMode.HALF_UP))
    .getOrElse(BigDecimal(0))


  def expectedSuncrystals: Long = totalKills/6
  def expectedScrolls: Long = (totalKills*247)/300
  def expectedDonuts: Long = totalKills/100

  def suncrystalOffset: Double = ((scCount-expectedSuncrystals)*100.00)/expectedSuncrystals
  def scrollsOffset: Double =  ((scCount-expectedScrolls)*100.00)/expectedScrolls
  def donutsOffset: Double =  ((donutCount-expectedDonuts)*100.00)/expectedDonuts

  def increaseBoomerStamps: User = User(userId, serverId, lastRoll, scCount, donutCount, scrollCount, boomerStampCount + 1, id)
  def increaseSC: User = User(userId, serverId, lastRoll = LocalDate.now(), scCount + 1, donutCount, scrollCount, boomerStampCount, id)
//  def donutPenalty: User = User(userId, serverId, lastRoll = LocalDate.now(), scCount - 3, donutCount + 1, scrollCount, boomerStampCount, id)
  def donutPenalty: User = User(userId, serverId, lastRoll = LocalDate.now(), 0, donutCount + 1, scrollCount, boomerStampCount, id)
  def increaseScroll: User = User(userId, serverId, lastRoll = LocalDate.now(), scCount, donutCount, scrollCount + 1, boomerStampCount, id)

}
