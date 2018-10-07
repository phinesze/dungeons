package game.item

import game.field.Field
import game.field.FieldBlock
import game.mold.Skill
import game.param.AbilityScore
import game.param.LevelAndExperience

/**
 * プレイヤー(Player)と敵キャラクター(Enemy)の基となる派生元クラス。GameObjectを継承する。
 * 次に行動できるまでの時間を表すtimeWaitを実装して、1カウント経過時の挙動を記述するmoveInCoutをオーバーライドして、
 * 経過ごとにtimeWaitを1減らしていき0になった場合にメソッドturnを呼び出し、その後にtimeWaitを一定値に戻す。
 * turnは派生先のプレイヤー/敵キャラクターでオーバーライドされる。
 */
abstract class GameCharacter(name: String, display: String, val abilityScore: AbilityScore, field: Field, levelAndExp: LevelAndExperience) : GameObject(name, field, display) {

    companion object {

        /**
         * timeWaitの初期値
         * timeWaitが0以下になった場合にはこの値を加算する。
         */
        val TIME_WAIT_START: Int = 1000
    }

    /**
     * プレイヤーまたは敵キャラクタが行動できるまでの時間(カウント)を表す。
     * 1カウント経過時に1減らす。
     */
    var timeWait: Int = TIME_WAIT_START

    /**
     * HPの現在地が0以下の場合か否かを表す
     */
    val isDead: Boolean
        get() = this.abilityScore.hp.now <= 0

    /**
     * 現在のレベルと経験値を表す。
     */
    open val levelAndExperience: LevelAndExperience = levelAndExp

    /**
     * 現在のレベルを取得する。
     */
    val level: Int
        get() = this.levelAndExperience.level

    /**
     * 現在の累積経験値を取得する。
     */
    val experience: Long
        get() = this.levelAndExperience.experience

    /**
     * 1カウント経過時の挙動を記述するGameObjectの同名関数をオーバーライドする。
     * timeWaitが1以上ある場合はagilityの分を減らし、 0になった場合はturnを呼び出す。timeWaitはturn終了後に一定の値に戻す。
     */
    override fun onCount() {
        if (this.timeWait <= 0) {
            turn()
            this.timeWait += TIME_WAIT_START
        } else {
            this.timeWait -= this.abilityScore.agility
        }
    }

    /**
     * プレイヤー/敵キャラクターはこの関数をオーバーライドして、行動可能時の行動を記述する。
     */
    open fun turn() {}

    /**
     * 別のゲームキャラクターに物理攻撃または魔法攻撃でダメージを与える。
     * @param target ダメージを受ける相手キャラクター
     * @param skill 物理通常攻撃の場合はnull、スキル攻撃の場合はtrue
     * @return 与えたダメージの量を表す数値
     */
    open fun attackTarget(target: GameCharacter, skill: Skill = Skill.normalAttack): Int {

        val targetScore = target.abilityScore
        val damage = skill.calculator(this.abilityScore, targetScore, skill)

        targetScore.hp.damage(damage)
        if (target.isDead) field.trashObject(target)
        return damage
    }

    /**
     * フィールド上の指定された位置から移動して敵キャラクタ/プレイヤーに当たった場合に
     * そのキャラクターを相手に指定された動作を実行する。
     * @param moveX 探索する際のフィールドのx移動量
     * @param moveY 探索する際のフィールドのy移動量
     * @param isWallTransfixable 壁を貫通するか否か
     * @param isObjectTransfixable 通過不可なゲームオブジェクトを貫通するか否か
     * @param action 指定する動作
     */
    fun actionLinear(
            moveX: Int,
            moveY: Int,
            isWallTransfixable: Boolean = false,
            isObjectTransfixable: Boolean = false,
            action: (GameCharacter) -> Unit
    ) {
        var x = this.position.x
        var y = this.position.y

        for (i in 0..9999) {
            x += moveX
            y += moveY

            val fieldBlock: FieldBlock = this.field.tryToGetFieldBlock(x, y) ?: return
            if (!isWallTransfixable && !fieldBlock.type.isFloor) return

            for (gameObject in fieldBlock.gameObjects) {
                if (!gameObject.isTraversable) {
                    if (gameObject is GameCharacter) action(gameObject)
                    if (!isObjectTransfixable) return
                }
            }
        }
    }

    protected fun moveLeft(): Boolean = tryToMove(position.x - 1, position.y)

    protected fun moveRight(): Boolean = tryToMove(position.x + 1, position.y)

    protected fun moveUp(): Boolean = tryToMove(position.x, position.y - 1)

    protected fun moveDown(): Boolean = tryToMove(position.x, position.y + 1)
    /**
     * スキルを実行する。現在のMPがスキルのMPコスト以上ある場合はスキルの動作を実行して、MPを消費して、スキル特有の待ち時間を加算する。
     * 現在のMPがMPコスト未満の場合は中断する。
     * @param skill 実行するスキル
     * @param target 実行する対象。魔法攻撃はnullとなる場合が多い。
     * @return スキルの実行された場合はtrue、中断された場合はfalse
     */
    fun executeSkill(skill: Skill, target: GameCharacter? = null): Boolean {

        if (this.abilityScore.mp.now < skill.mpCost) return false
        this.abilityScore.mp.now -= skill.mpCost
        this.timeWait += skill.timeCostPlus

        skill.action(skill, this, target)
        return true
    }

}