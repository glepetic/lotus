package org.maple
package services

import mappers.UserMapper
import model.{Drop, DropType, LilynouchResult, User}
import repositories.SCUsersRepository
import utils.OptionUtils.OptionImprovements

import scala.collection.immutable.HashMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

class StatsService {

  private val repository: SCUsersRepository = SCUsersRepository.getInstance
  private val mapper: UserMapper = new UserMapper
  private val actionMap = HashMap(
    Drop.SCROLL -> ((user:User) => user.increaseScroll),
    Drop.SUNCRYSTAL -> ((scUser:User) => scUser.increaseSC),
    Drop.DONUT -> ((scUser:User) => scUser.increaseDonut)
  )

  def findUser(userId: String, serverId: String): Future[User] = repository
    .find(userId, serverId)
    .map(opt => opt.map(mapper.to)
        .getOrElse(newUser(userId, serverId)))

  def fightLilynouch(userId: String, serverId: String, ignoreCooldown: Boolean = false): Future[DropType] = {
    this.findUser(userId, serverId)
      .filter(usr => ignoreCooldown || usr.canDoLilynouch)
      .map(user => {
        val afterLily = this.fightLilynouch(user)
        this.save(afterLily.scUser)
        afterLily.drop
      })
  }

  def guaranteedBoomerStamp(userId: String, discordServerId: String): Future[Unit] = {
    this.findUser(userId, discordServerId)
      .map(usr => usr.increaseBoomerStamps)
      .map(this.save)
  }

  private def save(user: User): Unit = {
    val document = mapper.to(user)
    Option(user)
      .filter(usr => usr.totalKills > 1)
      .map(_ => usr => repository.replace(usr))
      .getOrElse(usr => repository.insert(usr))
      .apply(document)
  }

  private def fightLilynouch(user: User): LilynouchResult = {
    val rnd = Random.nextInt(300)
    val sc = Option(rnd)
      .filter(value => 0 <= value && value < 50)
      .map(_ => Drop.SUNCRYSTAL)
    val donut = Option(rnd)
      .filter(value => 50 <= value && value < 53)
      .map(_ => Drop.DONUT)

    val dropType: DropType = sc.orElse(donut).getOrElse(Drop.SCROLL)

    LilynouchResult(actionMap.get(dropType).orThrow.apply(user), dropType)
  }

  def newUser(userId: String, serverId: String): User = User(userId, serverId, null, 0, 0, 0, 0)

}

object StatsService {
  private val instance: StatsService = new StatsService()
  def getInstance: StatsService = instance
}









