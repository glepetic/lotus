package org.maple
package services

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

class TrollService {

  val scService: SCService = SCService.getInstance

  def getRissaIGNDK(userId: String, discordServerId: String): Future[String] = {
    val lettersAsRandomString = (letters: List[String]) => Random.shuffle(letters).mkString("")

    // Xiuhacoatl
    val firstLetters = List("i", "u", "h", "a")
    val midLetters = List("c", "o", "a")
    val endLetters = List("t", "l")

    val shuffledName = "X" + lettersAsRandomString(firstLetters) + lettersAsRandomString(midLetters) + lettersAsRandomString(endLetters)

    Future(shuffledName)
      .filter(name => name.equalsIgnoreCase("Xiuhacoatl"))
      .flatMap(_ => scService.guaranteedSC(userId, discordServerId))
      .map(_ => "<:suncrystal:1026148453954371664> I have received a crafting material from Rissa!")
      .fallbackTo(Future(shuffledName))

  }

}

object TrollService {
  private val instance: TrollService = new TrollService()

  def getInstance: TrollService = instance
}







