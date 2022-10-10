package org.maple
package services

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

class TrollService {

  val statsService: StatsService = StatsService.getInstance
  val rissadkName: String = "Xiuhacoatl"

  def getRissaIGNDK(userId: String, discordServerId: String): Future[String] = {
    val lettersAsRandomString = (letters: List[String]) => Random.shuffle(letters).mkString("")

    // Xiuhacoatl
    val firstLetters = List("i", "u", "h", "a")
    val midLetters = List("c", "o", "a")
    val endLetters = List("t", "l")

    val shuffledName = "X" + lettersAsRandomString(firstLetters) + lettersAsRandomString(midLetters) + lettersAsRandomString(endLetters)

    Future(shuffledName)
      .filter(name => name.equalsIgnoreCase(rissadkName))
      .flatMap(_ => statsService.guaranteedBoomerStamp(userId, discordServerId))
      .map(_ => s"<@!$userId> An awkward person approaches and stares directly at you.\n" +
        "\"Wow, you've spelt it right, these boomers have been mispelling it all day. Here take this!\"\n" +
        s"She hands you a picture of herself with an autograph that reads *$rissadkName*. Apparently she is too boomer herself to spell it right...\n" +
        s"@everyone Congratulate <@!$userId> on obtaining a boomer stamp! <:boomerissa:1028876085976375296>"
      )
      .fallbackTo(Future(shuffledName))

  }

}

object TrollService {
  private val instance: TrollService = new TrollService()

  def getInstance: TrollService = instance
}







