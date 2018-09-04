package game.item

import game.param.AbilityScore

/**
 * プレイヤー(Player)と敵キャラクター(Enemy)の基となる派生元クラス。GameObjectを継承する。
 * 次に行動できるまでの時間を表すtimeWaitを実装して、1カウント経過時の挙動を記述するmoveInCoutをオーバーライドして、
 * 経過ごとにtimeWaitを1減らしていき0になった場合にメソッドturnを呼び出し、その後にtimeWaitを一定値に戻す。
 * turnは派生先のプレイヤー/敵キャラクターでオーバーライドされる。
 */
abstract class GameCharactor(name: String, var abilityScore: AbilityScore) : GameObject(name = name) {

    /**
     * プレイヤーまたは敵キャラクタが行動できるまでの時間(カウント)を表す。
     * 1カウント経過時に1減らす。
     */
    var timeWait :Int = 300

    /**
     * 1カウント経過時の挙動を記述するGameObjectの同名関数をオーバーライドする。
     * timeWaitが1以上ある場合は1減らし、 0になった場合はturnを呼び出す。timeWaitはturn終了後に一定の値に戻す。
     */
    override fun moveInCount() {

        if (timeWait <= 0) {
            turn()
            timeWait = 300
        } else {
            timeWait--
        }
    }

    /**
     * プレイヤー/敵キャラクターはこの関数をオーバーライドして、行動可能時の行動を記述する。
     */
    open fun turn() {}

}