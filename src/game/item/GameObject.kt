package game.item

import game.NamableObject
import game.field.Field
import game.param.Position

/**
 * 名前と位置情報を持つオブジェクトを表す抽象クラス。プレイヤー、敵キャラクター、アイテムなどはすべてこのクラスを継承する。
 * このクラスから派生するクラスのすべてのオブジェクトは1つのフィールドオブジェクト(field)に所属する。
 */
 abstract class GameObject(name: String) : NamableObject(name) {

    /**
     * 自身が存在しているフィールドを表す
     */
    internal var field: Field? = null

    /**
     * 自身のフィールド内での位置を表す
     */
    val position: Position = Position(0, 0)

    /**
     * 表示
     */
    abstract fun display() : String

    /**
     * ゲームボード上での1カウント経過時の挙動を記述することができる抽象関数
     * 例としてプレイヤー(Player)または敵キャラクター(Enemy)の場合は、カウントを1減らして0になった場合に特定の行動を取らせる。
     */
    open fun moveInCount() {}

    /**
     * フィールド内での指定したx, y位置への移動を試みる。
     * 移動先に指定したx, y位置がフィールド内で移動可能なブロックの場合は移動を完了してtrueを返す。
     * 移動可能なブロックでない場合はfalseを返す。
     * @param x: Int 移動先のx位置
     * @param y: Int 移動先のy位置
     */
    protected fun tryToMove(x: Int, y: Int) : Boolean {

        val fieldBlock = this.field?.tryToGetFieldBlock(x, y)
        if (fieldBlock?.type?.isFloor !== true) return false

        try { this.field!!.moveObject(this, x, y) } catch (e: Exception) { return false }
        return true
    }
}