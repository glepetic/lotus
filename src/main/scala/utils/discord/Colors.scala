package org.maple
package utils.discord

import utils.JsonUtils
import utils.OptionUtils._

import spray.json.{JsNumber, JsValue}

object Colors {

  private val colors: JsValue = JsonUtils.readJson("/colors.json")

  def get(color: String): Int = colors.asJsObject.fields.get(color).map(c => c.asInstanceOf[JsNumber].value.toInt).orThrow

}
