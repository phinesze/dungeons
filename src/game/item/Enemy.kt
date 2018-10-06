package game.item

import game.field.Field
import game.mold.EnemyMold
import game.param.AbilityScore
import game.param.LevelAndExperience

/**
 * 敵キャラクターを表すクラス。GameCharacterを継承する。
 */
class Enemy(
        name: String,
        display: String,
        abilityScore: AbilityScore,
        field: Field,
        levelAndExp: LevelAndExperience
) : GameCharacter(name, display, abilityScore, field, levelAndExp) {

    override val levelAndExperience: LevelAndExperience = levelAndExp

    /**
     *  敵キャラクターのモールド(EnemyMold)から敵キャラクターを生成する。
     */
    constructor(mold: EnemyMold, level: Int, field: Field) : this(
            mold.name,
            mold.display,
            AbilityScore(mold.abilityMap[level]!!),
            field,
            LevelAndExperience(level,0)
    )

    /**
     *  敵キャラクターの行動可能時の動作
     * プレイヤーキャラクターに直線状に近づくように移動する。
     */
    override fun turn() {

        if (isDead) return
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

    /**
     * フィールド上のプレイヤーと衝突した場合にプレイヤーを攻撃する動作を行う。
     * GameItemの同名関数をオーバーライドする。
     */
    override fun collisionDetected(otherObject: GameObject) {
        if (otherObject is Player) {
            attackPlayer(otherObject)
        }
    }

    /**
     * プレイヤーにダメージを与える
     * @param player ダメージを受けるプレイヤー
     */
    private fun attackPlayer(player: Player) {
        val damage = this.attackTarget(player)
        println("${player.name}は${this.name}に${damage}ダメージを受けた")
        if (player.abilityScore.hp.now <= 0) println("${player.name}は死んでしまった")
    }
}