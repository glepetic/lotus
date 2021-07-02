package org.maple
package utils

object OptionUtils {

  implicit class OptionImprovements[A](val opt: Option[A]) {
    def orThrow: A = this.orThrow(() => new RuntimeException)
    def orThrow(exc: () => Exception): A = {
      opt match {
        case Some(a) => a
        case None => throw exc.apply()
      }
    }
  }

}
