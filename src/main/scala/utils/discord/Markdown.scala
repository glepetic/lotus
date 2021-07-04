package org.maple
package utils.discord

object Markdown {
  def bold(str: String): String = "**" + str + "**"
  def strikethrough(str: String): String = "~~" + str + "~~"
  def italics(str: String): String = "*" + str + "*"
  def underline(str: String): String = "__" + str + "__"
  def codeSnippet(str: String): String = "```" + str + "```"
  def hightlight(str: String): String = "`" + str + "`"
}
