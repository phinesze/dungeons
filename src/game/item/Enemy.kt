package game.item

import game.mold.EnemyMold
import game.param.AbilityScore

class Enemy(name: String, abilityScore: AbilityScore): GameCharactor(name, abilityScore) {

    /**
     *  敵キャラクターのモールド(EnemyMold)から敵キャラクターを生成する。
     */
    constructor(mold: EnemyMold) : this(mold.name,  mold.abilityScore)

    override fun display(): String = "EE"
}