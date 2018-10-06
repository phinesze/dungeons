package game.item

import game.field.Field

/**
 *  フィールド上で拾えるアイテムを表すクラス。GameObjectを継承する。
 */
class GameItem(name: String, field: Field) : GameObject(name, field, "") {

    init {
        this.display = ""
        this.isThroughable = true
    }
}