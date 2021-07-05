package org.maple
package commands

import ackcord.commands.UserCommandMessage
import ackcord.requests.Request

trait MyCommand {
    def aliases: Seq[String]
    def execute(msg: UserCommandMessage[_], arguments: List[String]) : Request[_]
}
