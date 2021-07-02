package org.maple
package utils

import config.BotEnvironment

import spray.json._

import scala.io.Source

object JsonUtils {

  def readJson(fileName: String): JsValue = {
    val source = Source.fromFile(BotEnvironment.resourcesPath + fileName)
    val lines = try source.mkString finally source.close()
    lines.parseJson
  }

}
