package game.item

import game.field.Field
import game.mold.EnemyMold
import game.param.AbilityScore

class Enemy(name: String, abilityScore: AbilityScore, field: Field): GameCharactor(name, abilityScore, field) {

    /**
     *  敵キャラクターのモールド(EnemyMold)から敵キャラクターを生成する。
     */
    constructor(mold: EnemyMold, field: Field) : this(mold.name,  mold.abilityScore.clone(), field)

    override fun turn() {
        moveLeft()
        val arrowMap = field.arrowMap

        arrowMap.getLeftSideArrow(this.position.x,  this.position.y)
        arrowMap.getTopSideArrow(this.position.x,  this.position.y)
        arrowMap.getRightSideArrow(this.position.x,  this.position.y)
        arrowMap.getBottomSideArrow(this.position.x,  this.position.y)

        //TODO("敵の行動")
    }

    override fun display(): String = "EE"
}