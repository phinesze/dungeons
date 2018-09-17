package game.item

import game.field.Field
import game.mold.EnemyMold
import game.param.AbilityScore

class Enemy(name: String, abilityScore: AbilityScore, field: Field): GameCharactor(name, abilityScore, field) {

    /**
     *  敵キャラクターのモールド(EnemyMold)から敵キャラクターを生成する。
     */
    constructor(mold: EnemyMold, field: Field) : this(mold.name,  mold.abilityScore.clone(), field)

    /**
     *  敵キャラクターの行動可能時の動作
     * プレイヤーキャラクターに直線状に近づくように移動する。
     */
    override fun turn() {
        //プレイヤーオブジェクトを取得
        val player = field.getPlayer(0)
        //敵キャラクターとプレイヤーとの間のx, y位置の差を取得
        var x = player.position.x - this.position.x
        var y = player.position.y - this.position.y
        //上下左右どちらに移動するかを決定
        if (Math.abs(x) > Math.abs(y)) {
            if (x < 0) this.moveLeft() else this.moveRight()
        } else {
            if (y < 0) this.moveUp() else this.moveDown()
        }
    }

    override fun display(): String = "EE"
}