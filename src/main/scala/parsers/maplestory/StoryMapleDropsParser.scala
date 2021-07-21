package org.maple
package parsers.maplestory

import model.maplestory.{MapleItem, MapleMob}

import spray.json.{JsArray, JsString, JsValue}

class StoryMapleDropsParser {

  def parse(dropsJson: JsValue): List[MapleItem] = {
    val data = dropsJson.asJsObject.getFields("data").head.asInstanceOf[JsArray].elements
    val mobs = data.map(element => this.parse(element.asInstanceOf[JsArray]))
    mobs.foldLeft(Nil: List[MapleItem])(this.accumulateDrop)
  }

  def parse(mobArray: JsArray): MapleItem = MapleItem(mobArray.elements(1).asInstanceOf[JsString].value, List(mobArray.elements(0).asInstanceOf[JsString].value))

  private def accumulateDrop(drops: List[MapleItem], drop: MapleItem): List[MapleItem] = {
    val updatedMobDrop = drops
      .find(d => d.name equalsIgnoreCase drop.name)
      .map(d => MapleItem(d.name, d.mobs ++ drop.mobs))
      .getOrElse(drop)

    drops.filterNot(m => m.name equals drop.name) ++ List(updatedMobDrop)
  }

}
