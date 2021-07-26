package org.maple
package model.maplestory

import utils.OptionUtils._

object Jobs {

  val bs = "Bishop"
  val fp = "F/P Archmage"
  val il = "I/L Archmage"
  val bm = "Bowmaster"
  val mm = "Marksman"
  val nl = "Night Lord"
  val shad = "Shadower"
  val bucc = "Buccaneer"
  val sair = "Corsair"
  val dk = "Dark Knight"
  val hero = "Hero"
  val pally = "Paladin"

  val branches = List(bs, fp, il, bm, mm, nl, shad, bucc, sair, dk, hero, pally)

  //TODO: dependency injection
  private val values: List[MapleJob] = List(new Bishop, new Priest, new Cleric, new FPArchmage, new FPMage, new FPWizard,
    new ILArchmage, new ILMage, new ILWizard, new Bowmaster, new Ranger, new Hunter, new Marksman, new Sniper, new Crossbowman,
    new NightLord, new Hermit, new Assassin, new Shadower, new ChiefBandit, new Bandit, new Buccaneer, new Marauder, new Brawler,
    new Corsair, new Outlaw, new Gunslinger, new DarkKnight, new DragonKnight, new Spearman, new Hero, new Crusader, new Fighter,
    new Paladin, new WhiteKnight, new Page, new Magician, new Bowman, new Thief, new Pirate, new Warrior, new Beginner)

  trait MapleJob {
    def name: String
    def branch: String
  }

  def of(jobString: String): MapleJob = values
    .find(j => j.name.toLowerCase equals jobString.toLowerCase)
    .orThrow

  class Bishop extends MapleJob {
    override def name: String = "Bishop"
    override def branch: String = bs
  }

  class Priest extends MapleJob {
    override def name: String = "Priest"
    override def branch: String = bs
  }

  class Cleric extends MapleJob {
    override def name: String = "Cleric"
    override def branch: String = bs
  }

  class FPArchmage extends MapleJob {
    override def name: String = "F/P Archmage"
    override def branch: String = fp
  }

  class FPMage extends MapleJob {
    override def name: String = "F/P Mage"
    override def branch: String = fp
  }

  class FPWizard extends MapleJob {
    override def name: String = "F/P Wizard"
    override def branch: String = fp
  }

  class ILArchmage extends MapleJob {
    override def name: String = "I/L Archmage"
    override def branch: String = il
  }

  class ILMage extends MapleJob {
    override def name: String = "I/L Mage"
    override def branch: String = il
  }

  class ILWizard extends MapleJob {
    override def name: String = "I/L Wizard"
    override def branch: String = il
  }

  class Bowmaster extends MapleJob {
    override def name: String = "Bowmaster"
    override def branch: String = bm
  }

  class Ranger extends MapleJob {
    override def name: String = "Ranger"
    override def branch: String = bm
  }

  class Hunter extends MapleJob {
    override def name: String = "Hunter"
    override def branch: String = bm
  }

  class Marksman extends MapleJob {
    override def name: String = "Marksman"
    override def branch: String = mm
  }

  class Sniper extends MapleJob {
    override def name: String = "Sniper"
    override def branch: String = mm
  }

  class Crossbowman extends MapleJob {
    override def name: String = "Crossbowman"

    override def branch: String = mm
  }

  class NightLord extends MapleJob {
    override def name: String = "Night Lord"
    override def branch: String = nl
  }

  class Hermit extends MapleJob {
    override def name: String = "Hermit"
    override def branch: String = nl
  }

  class Assassin extends MapleJob {
    override def name: String = "Assassin"
    override def branch: String = nl
  }

  class Shadower extends MapleJob {
    override def name: String = "Shadower"
    override def branch: String = shad
  }

  class ChiefBandit extends MapleJob {
    override def name: String = "Chief Bandit"
    override def branch: String = shad
  }

  class Bandit extends MapleJob {
    override def name: String = "Bandit"
    override def branch: String = shad
  }

  class Buccaneer extends MapleJob {
    override def name: String = "Buccaneer"
    override def branch: String = bucc
  }

  class Marauder extends MapleJob {
    override def name: String = "Marauder"
    override def branch: String = bucc
  }

  class Brawler extends MapleJob {
    override def name: String = "Brawler"
    override def branch: String = bucc
  }

  class Corsair extends MapleJob {
    override def name: String = "Corsair"
    override def branch: String = sair
  }

  class Outlaw extends MapleJob {
    override def name: String = "Outlaw"
    override def branch: String = sair
  }

  class Gunslinger extends MapleJob {
    override def name: String = "Gunslinger"
    override def branch: String = sair
  }

  class DarkKnight extends MapleJob {
    override def name: String = "Dark Knight"
    override def branch: String = dk
  }

  class DragonKnight extends MapleJob {
    override def name: String = "Dragon Knight"
    override def branch: String = dk
  }

  class Spearman extends MapleJob {
    override def name: String = "Spearman"
    override def branch: String = dk
  }

  class Hero extends MapleJob {
    override def name: String = "Hero"
    override def branch: String = hero
  }

  class Crusader extends MapleJob {
    override def name: String = "Crusader"
    override def branch: String = hero
  }

  class Fighter extends MapleJob {
    override def name: String = "Fighter"
    override def branch: String = hero
  }

  class Paladin extends MapleJob {
    override def name: String = "Paladin"
    override def branch: String = pally
  }

  class WhiteKnight extends MapleJob {
    override def name: String = "White Knight"
    override def branch: String = pally
  }

  class Page extends MapleJob {
    override def name: String = "Page"
    override def branch: String = pally
  }

  class Magician extends MapleJob {
    override def name: String = "Magician"
    override def branch: String = null
  }

  class Bowman extends MapleJob {
    override def name: String = "Bowman"
    override def branch: String = null
  }

  class Thief extends MapleJob {
    override def name: String = "Thief"
    override def branch: String = null
  }

  class Pirate extends MapleJob {
    override def name: String = "Pirate"
    override def branch: String = null
  }

  class Warrior extends MapleJob {
    override def name: String = "Warrior"
    override def branch: String = null
  }

  class Beginner extends MapleJob {
    override def name: String = "Beginner"
    override def branch: String = null
  }

}
