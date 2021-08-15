package org.maple
package commands

import ackcord.commands.UserCommandMessage
import ackcord.requests.Request
import org.slf4j.{Logger, LoggerFactory}

trait MyCommand {
    val logger: Logger = LoggerFactory.getLogger(this.getClass)
    def aliases: Seq[String]
    def execute(msg: UserCommandMessage[_], arguments: List[String]) : Request[_]
}
