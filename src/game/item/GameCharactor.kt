package game.item

import game.param.AbilityScore

abstract class GameCharactor(name: String, var abilityScore: AbilityScore) : GameObject(name = name) {

    var timeWait :Int = 300

    override fun moveInCount() {

        if (timeWait <= 0) {
            turn()
        } else {
            timeWait--
        }
    }

    open fun turn() {
        timeWait = 300
    }

}