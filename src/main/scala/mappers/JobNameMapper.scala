package org.maple
package mappers

import scala.language.postfixOps

class JobNameMapper {

  private val jobs = Map(
    ("bs", "Bishop"),
    ("fp", "F/P Archmage"),
    ("il", "I/L Archmage"),
    ("bm", "Bowmaster"),
    ("mm", "Marksman"),
    ("nl", "Night Lord"),
    ("shad", "Shadower"),
    ("bucc", "Buccaneer"),
    ("sair", "Corsair"),
    ("dk", "Dark Knight"),
    ("hero", "Hero"),
    ("pally", "Paladin")
  )

  def toFullName(key: String): Option[String] = this.jobs.get(key toLowerCase)

}
