package game.item

import game.field.Field
import game.param.AbilityScore
import game.param.LevelAndExperience

/**
 * プレイヤー(Player)と敵キャラクター(Enemy)の基となる派生元クラス。GameObjectを継承する。
 * 次に行動できるまでの時間を表すtimeWaitを実装して、1カウント経過時の挙動を記述するmoveInCoutをオーバーライドして、
 * 経過ごとにtimeWaitを1減らしていき0になった場合にメソッドturnを呼び出し、その後にtimeWaitを一定値に戻す。
 * turnは派生先のプレイヤー/敵キャラクターでオーバーライドされる。
 */
abstract class GameCharactor(name: String, display: String, val abilityScore: AbilityScore, field: Field, levelAndExp: LevelAndExperience) : GameObject(name, field) {

    /**
     * 表示
     */
    var display: String = display
    /**
     * プレイヤーまたは敵キャラクタが行動できるまでの時間(カウント)を表す。
     * 1カウント経過時に1減らす。
     */
    var timeWait: Int = 300

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
    val level : Int
            get() = this.levelAndExperience.level
    /**
     * 現在の累積経験値を取得する。
     */
    val experience : Long
            get() = this.levelAndExperience.experience

    /**
     * 1カウント経過時の挙動を記述するGameObjectの同名関数をオーバーライドする。
     * timeWaitが1以上ある場合は1減らし、 0になった場合はturnを呼び出す。timeWaitはturn終了後に一定の値に戻す。
     */
    override fun onCount() {
        if (this.timeWait <= 0) {
            turn()
            this.timeWait = 300
        } else {
            this.timeWait--
        }
    }

    /**
     * プレイヤー/敵キャラクターはこの関数をオーバーライドして、行動可能時の行動を記述する。
     */
    open fun turn() {}

    /**
     * 別のゲームキャラクターにダメージを与える。
     *  @param target ダメージを受ける相手キャラクター
     * @return 与えたダメージの量を表す数値
     */
    fun attackTarget(target: GameCharactor): Int {

        val thisAttack = this.abilityScore.abilityMold.attack
        val targetDefense = target.abilityScore.abilityMold.defense

        val damage = thisAttack - targetDefense / 2
        target.abilityScore.hp.damage(damage)

        if (target.abilityScore.hp.now <= 0) field.trashObject(target)

        return damage
    }

    protected fun moveLeft(): Boolean = tryToMove(position.x - 1, position.y)
    protected fun moveRight(): Boolean = tryToMove(position.x + 1, position.y)
    protected fun moveUp(): Boolean = tryToMove(position.x, position.y - 1)
    protected fun moveDown(): Boolean = tryToMove(position.x, position.y + 1)

    /**
     * 表示する。
     */
    override fun display(): String = display
}