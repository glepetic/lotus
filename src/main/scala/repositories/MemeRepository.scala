package org.maple
package repositories

import utils.JsonUtils

import spray.json.{JsArray, JsString, JsValue}

import scala.util.Random

class MemeRepository {

  val memes: JsValue = JsonUtils.readJson("/memes.json")

  def getMaplestoryMeme: Option[String] = this.memes.asJsObject.fields.get("maplestory")
    .map(mapleMemes => {
      val elements = mapleMemes.asInstanceOf[JsArray].elements
      elements(Random.nextInt(elements.length)).asInstanceOf[JsString].value
    })

}

object MemeRepository {
  private val instance: MemeRepository = new MemeRepository()
  def getInstance: MemeRepository = instance
}
