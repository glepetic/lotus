package org.maple
package model

case class Player(rank: Int, characters: List[MapleCharacter]) {
  def nirvana: Int = this.topUniqueBranchCharacters.map(c => c.level).filter(lvl => lvl > 200).sum
  def linkLevels: Int = this.topUniqueBranchCharacters.map(c => c.level).filter(lvl => lvl >= 30).sum

  def hasCharacterOfJobBranch(jobBranch: String): Boolean = this.characters.exists(char => jobBranch equals char.job.branch())

  def topUniqueBranchCharacters : List[MapleCharacter] = Jobs.branches
    .filter(branch => this.hasCharacterOfJobBranch(branch))
    .map(job => this.highestLevelOfJobBranch(job))

  def highestLevelOfJobBranch(jobBranch: String): MapleCharacter =
    this.characters
      .sortBy(char => char.level)
      .findLast(char => jobBranch equals char.job.branch())
      .orNull
}
