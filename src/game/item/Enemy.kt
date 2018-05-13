package game.item

import game.param.AbilityScore

class Enemy(name: String, abilityScore: AbilityScore): GameCharactor(name, abilityScore) {
    override fun display(): String = "EE"
}