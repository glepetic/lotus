package org.maple
package services

import clients.StoryMapleWebsiteClient
import model.maplestory.MapleItem
import parsers.maplestory.StoryMapleDropsParser

class MobDropsService {

  val client = new StoryMapleWebsiteClient()
  val parser = new StoryMapleDropsParser()

  def getDrops: List[MapleItem] = client.getMobDrops.map(rjson => this.parser.parse(rjson)).getOrElse(Nil)

  def getDrops(mobName: String): List[MapleItem] = this.getDrops.filter(d => d.mobs.map(_.toLowerCase).contains(mobName.toLowerCase))
  def getDrop(itemName: String): Option[MapleItem] = this.getDrops.find(d => d.name equalsIgnoreCase itemName)

}

object MobDropsService {
  private val instance: MobDropsService = new MobDropsService()

  def getInstance: MobDropsService = instance
}


