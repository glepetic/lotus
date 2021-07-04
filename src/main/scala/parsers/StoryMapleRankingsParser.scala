package org.maple
package parsers

import model.Player

import org.maple.model.maplestory.{Jobs, MapleCharacter, Player}
import spray.json.{JsArray, JsNumber, JsString, JsValue}

class StoryMapleRankingsParser {

  def parse(rankingsJson: JsValue): List[Player] = {
    val data = rankingsJson.asJsObject.getFields("data").head.asInstanceOf[JsArray].elements
    val characters = data.map(element => this.parse(element.asInstanceOf[JsArray]))
    characters.foldLeft(Nil: List[Player])((accum, char) => this.accumulatePlayerCharacter(accum, char))
  }

  private def parse(characterArray: JsArray): Player = {
    val char = MapleCharacter(
      characterArray.elements(1).asInstanceOf[JsString].value,
      characterArray.elements(4).asInstanceOf[JsNumber].value.toInt,
      Jobs.of(characterArray.elements(3).asInstanceOf[JsString].value),
      // TODO: map Guild
      Option(characterArray.elements(2)).filter(g => g.isInstanceOf[JsString]).map(g => g.asInstanceOf[JsString].value).orNull)

    Player(characterArray.elements(0).asInstanceOf[JsNumber].value.toInt, List(char))
  }

  private def accumulatePlayerCharacter(players: List[Player], player: Player): List[Player] = {
    val updatedPlayer = players
      .find(p => p.rank equals player.rank)
      .map(p => Player(p.rank, p.characters ++ player.characters))
      .getOrElse(player)

    players.filterNot(p => p.rank equals updatedPlayer.rank) ++ List(updatedPlayer)
  }

}
