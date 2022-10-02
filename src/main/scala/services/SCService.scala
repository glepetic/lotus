package org.maple
package services

import mappers.SCUserMapper
import model.{Drop, DropType, LilynouchResult, SCUser}
import repositories.SCUsersRepository
import utils.OptionUtils.OptionImprovements

import scala.collection.immutable.HashMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

class SCService {

  private val repository: SCUsersRepository = SCUsersRepository.getInstance
  private val mapper: SCUserMapper = new SCUserMapper
  private val actionMap = HashMap(
    Drop.SCROLL -> ((scUser:SCUser) => scUser.scrollIncreaser),
    Drop.SUNCRYSTAL -> ((scUser:SCUser) => scUser.scIncreaser),
    Drop.DONUT -> ((scUser:SCUser) => scUser.donutIncreaser)
  )

  def fightLilynouch(userId: String, serverId: String): Future[DropType] = {
    repository.find(userId, serverId)
      .map(opt => opt.map(mapper.to)
        .getOrElse(newSCUser(userId, serverId))
      )
      .filter(_.canDoLilynouch)
      .map(scUser => {
        val afterLily = this.fightLilynouch(scUser)
        Option(scUser.lastRoll)
          .map(_ => usr => repository.replace(usr))
          .getOrElse(usr => repository.insert(usr))
          .apply(mapper.to(afterLily.scUser))
        repository.insert(mapper.to(afterLily.scUser))
        afterLily.drop
      })
  }

  private def fightLilynouch(scUser: SCUser): LilynouchResult = {
    val rnd = Random.nextInt(300)
    val sc = Option(rnd)
      .filter(value => 0 <= value && value < 50)
      .map(_ => Drop.SUNCRYSTAL)
    val donut = Option(rnd)
      .filter(value => 50 <= value && value < 53)
      .map(_ => Drop.DONUT)

    val dropType: DropType = sc.orElse(donut).getOrElse(Drop.SCROLL)

    LilynouchResult(actionMap.get(dropType).orThrow.apply(scUser), dropType)
  }

  def newSCUser(userId: String, serverId: String): SCUser = SCUser(userId, serverId, null, 0, 0, 0)

}

object SCService {
  private val instance: SCService = new SCService()
  def getInstance: SCService = instance
}









