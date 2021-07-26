package org.maple
package mappers

import dto.{BossRosterDto, RaiderDto}
import model.maplestory.Player

class BossRosterMapper {

  def toRaider(player: Player, igns: Seq[String]): Option[RaiderDto] = {
    player.characters
      .find(char => igns.map(_.toLowerCase) contains char.ign.toLowerCase)
      .map(char => RaiderDto(player.linkLevels, char.ign, char.level, char.job))
  }

  def toBossRoster(players: List[RaiderDto]): BossRosterDto = BossRosterDto(players.sortBy(-_.links))

}
