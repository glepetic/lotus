package org.maple

import config.BotEnvironment

import ackcord._
import org.maple.utils.discord.ClientInitializer

import scala.util.{Failure, Success}

object Bot extends App {

  val clientSettings = ClientSettings(BotEnvironment.token)
  val futureClient = clientSettings.createClient()

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  futureClient onComplete {
    case Success(client) =>
      client.login()
      ClientInitializer.initialize(client)
      BotEnvironment.client = Option(client)
    case Failure(e) => println("Could not create client: " + e.getMessage)
  }

}
