package org.maple
package model.maplestory

case class MapleGuild(name: String, members: List[MapleCharacter]){
  def totalLevels: Int = this.members.map(c => c.level).sum
}
