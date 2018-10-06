package game.item

import game.field.Field
import game.mold.ItemMold

/**
 *  フィールド上で拾えるアイテムを表すクラス。GameObjectを継承する。
 */
class GameItem(mold: ItemMold, field: Field) : GameObject(mold.name, field, mold.display) {

    /**
     * アイテムを使用した場合の動作となる関数
     */
    val action: (GameCharacter) -> Unit = mold.action

    /**
     * フィールド上のプレイヤーと重なった場合にプレイヤーがアイテムを使用する動作を行う。
     * GameItemの同名関数をオーバーライドする。
     */
    override fun collisionDetected(otherObject: GameObject) {
        if (otherObject is Player) {
            this.action(otherObject)
            this.field.trashObject(this)
        }
    }

    init {
        this.isTraversable = true
    }
}