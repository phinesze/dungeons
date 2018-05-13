package game.item

import game.NamableObject
import game.param.Position

/**
 * 名前と位置情報を持つオブジェクト
 */
abstract class GameObject(name: String) : NamableObject(name) {

    /**
     * 位置
     */
    val position: Position = Position(0, 0)

    /**
     * 表示
     */
    abstract fun display() : String

    /**
     * ゲームボード上での1カウントの行動
     */
    open fun moveInCount() {}
}