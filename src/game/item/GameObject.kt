package game.item

import game.NamableObject
import game.field.Field
import game.field.FieldBlock
import game.param.Position

/**
 * 名前と位置情報を持つオブジェクトを表す抽象クラス。プレイヤー、敵キャラクター、アイテムなどはすべてこのクラスを継承する。
 * このクラスから派生するクラスのすべてのオブジェクトは1つのフィールドオブジェクト(field)に所属する。
 * @property field 自身が存在しているフィールド
 * @property field 自身の所属するフィールドをtoStringで出力した場合の自身のフィールド内での表示文字列
 */
abstract class GameObject(name: String, internal var field: Field, var display: String) : NamableObject(name) {

    /**
     *  自身が位置しているフィールド上のブロック
     */
    internal var fieldBlock: FieldBlock? = null

    /**
     *  ほかのオブジェクトの移動を妨げない個体であるかどうかを表す真偽値
     */
    internal var isTraversable: Boolean = false

    /**
     * 自身のフィールド内でのx, y位置を表す値
     */
    val position: Position = Position(0, 0)

    /**
     * ゲームボード上での1カウント経過時の挙動を記述することができる仮想関数
     * 例としてプレイヤー(Player)または敵キャラクター(Enemy)の場合は、カウントを1減らして0になった場合に特定の行動を取らせる。
     */
    open fun onCount() {}

    /**
     *  フィールド上のオブジェクト同士が重なった場合の挙動を記述することができる仮想関数
     */
    open fun collisionDetected(otherObject: GameObject) {}

    /**
     * フィールド上の指定された位置から移動して敵キャラクタ/プレイヤーに当たった場合に
     * そのキャラクターを相手に指定された動作を実行する。
     * @param moveX 探索する際のフィールドのx移動量
     * @param moveY 探索する際のフィールドのy移動量
     * @param canTransfix 壁を貫通するか否か
     * @param canTransfixObjects 通過不可なゲームオブジェクトを貫通するか否か
     * @param action 指定する動作
     */
    fun actionLinear(
            moveX: Int,
            moveY: Int,
            canTransfix: Boolean = false,
            canTransfixObjects: Boolean = false,
            action: (GameCharacter) -> Unit
    ) {
        var x = this.position.x
        var y = this.position.y

        for (i in 0..9999) {
            x += moveX
            y += moveY

            val fieldBlock: FieldBlock = this.field.tryToGetFieldBlock(x, y) ?: return
            if (!canTransfix && !fieldBlock.type.isFloor) return

            for (gameObject in fieldBlock.gameObjects) {
                if (!gameObject.isTraversable) {
                    if (gameObject is GameCharacter) action(gameObject)
                    if (!canTransfixObjects) return
                }
            }
        }
    }

    /**
     * フィールド内での指定したx, y位置への移動を試みる。
     * 移動先に指定したx, y位置がフィールド内で移動可能なブロックの場合は移動を完了してtrueを返す。
     * 移動可能なブロックでない場合はfalseを返す。
     * @param x: Int 移動先のx位置
     * @param y: Int 移動先のy位置
     */
    protected fun tryToMove(x: Int, y: Int) : Boolean {
        try {
            //フィールドブロックを取得して床ではない場合はfalse
            if (!field.getFieldBlock(x, y).type.isFloor) return false
            //衝突判定
            val otherObject = this.field.getNotTraversableObject(x, y)
            if (otherObject != null) { this.collisionDetected(otherObject); return true }
            //指定した位置への移動
            this.field.moveObject(x, y, this)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    /**
     * フィールド内での左への移動を試みる。
     * 移動可能なブロックの場合は移動を完了してtrueを返す。移動可能でない場合はfalseを返す。
     */
    protected fun moveLeft(): Boolean = tryToMove(position.x - 1, position.y)

    /**
     * フィールド内での左への移動を試みる。
     * 移動可能なブロックの場合は移動を完了してtrueを返す。移動可能でない場合はfalseを返す。
     */
    protected fun moveRight(): Boolean = tryToMove(position.x + 1, position.y)

    /**
     * フィールド内での左への移動を試みる。
     * 移動可能なブロックの場合は移動を完了してtrueを返す。移動可能でない場合はfalseを返す。
     */
    protected fun moveUp(): Boolean = tryToMove(position.x, position.y - 1)

    /**
     * フィールド内での左への移動を試みる。
     * 移動可能なブロックの場合は移動を完了してtrueを返す。移動可能でない場合はfalseを返す。
     */
    protected fun moveDown(): Boolean = tryToMove(position.x, position.y + 1)
}