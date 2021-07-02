package org.maple
package utils

object IterableUtils {

  implicit class IterableImprovements[A](val it: Iterable[A]) {

    def join: String = this.join("")
    def joinWords: String = this.join(" ")
    def joinLines: String = this.join("\n")
    def join(separator: String): String = it.foldLeft("")((acc, elem) => acc+elem+separator)

  }

}
