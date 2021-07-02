package org.maple
package utils

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

object DateTimeUtils {

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(s"E MMM d'th' '-' hh:mm a")

  def nowAtZone(zone: String): ZonedDateTime = ZonedDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get(zone)))
  def asString(d : ZonedDateTime): String = this.formatter.format(d)
    .replace("1th","1st")
    .replace("2th", "2nd")
    .replace("3th", "3rd")

}
