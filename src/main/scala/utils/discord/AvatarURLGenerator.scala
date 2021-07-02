package org.maple
package utils.discord

import ackcord.data.User

object AvatarURLGenerator {

  def generate(user: Option[User]): Option[String] =
    user.flatMap(u => u.avatar.map(avt => s"https://cdn.discordapp.com/avatars/${u.id}/${avt}.webp"))

}
