package org.maple
package services

import mappers.BossRunMapper
import model.maplestory.BossRun
import repositories.HostsRepository

import org.mongodb.scala.SingleObservable

class EventHostsService {

  private val hostsRepository: HostsRepository = HostsRepository.getInstance
  private val bossRunMapper: BossRunMapper = new BossRunMapper

  def insert(br: BossRun): Unit = this.hostsRepository.insert(this.bossRunMapper.to(br))
  def replace(br: BossRun): Unit = this.hostsRepository.replace(this.bossRunMapper.to(br))
  def find(messageId: String): SingleObservable[BossRun] = this.hostsRepository
    .find(messageId)
    .map(this.bossRunMapper.to)
  def findLatest(hostId: String, channelId: String): SingleObservable[BossRun] = this.hostsRepository
    .findLatest(hostId, channelId)
    .map(this.bossRunMapper.to)

}

object EventHostsService {
  private val instance: EventHostsService = new EventHostsService
  def getInstance: EventHostsService = instance
}







