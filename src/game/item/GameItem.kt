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

    init {
        this.isThroughable = true
    }
}