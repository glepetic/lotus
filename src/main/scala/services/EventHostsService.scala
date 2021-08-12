package org.maple
package services

import mappers.HostedEventMapper
import repositories.HostsRepository

import org.maple.model.HostedEvent
import org.mongodb.scala.SingleObservable

class EventHostsService {

  private val hostsRepository: HostsRepository = HostsRepository.getInstance
  private val hostedEventMapper: HostedEventMapper = new HostedEventMapper

  def insert(he: HostedEvent): Unit = this.hostsRepository.insert(this.hostedEventMapper.to(he))
  def replace(he: HostedEvent): Unit = this.hostsRepository.replace(this.hostedEventMapper.to(he))
  def find(messageId: String): SingleObservable[HostedEvent] = this.hostsRepository
    .find(messageId)
    .map(this.hostedEventMapper.to)
  def findLatest(hostId: String, channelId: String): SingleObservable[HostedEvent] = this.hostsRepository
    .findLatest(hostId, channelId)
    .map(this.hostedEventMapper.to)

}

object EventHostsService {
  private val instance: EventHostsService = new EventHostsService
  def getInstance: EventHostsService = instance
}







