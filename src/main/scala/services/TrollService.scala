package org.maple
package services

import scala.util.Random

class TrollService {

  def getRissaIGNDK: String = {
    val lettersAsRandomString = (letters: List[String]) => Random.shuffle(letters).mkString("")

    // Xiuhacoatl
    val firstLetters = List("i", "u", "h", "a")
    val midLetters = List("c", "o", "a")
    val endLetters = List("t", "l")

    "X" + lettersAsRandomString(firstLetters) + lettersAsRandomString(midLetters) + lettersAsRandomString(endLetters)
  }

}

object TrollService {
  private val instance: TrollService = new TrollService()

  def getInstance: TrollService = instance
}







