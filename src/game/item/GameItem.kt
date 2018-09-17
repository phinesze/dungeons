package game.item

import game.field.Field

/**
 *  フィールド上で拾えるアイテムを表すクラス。GameObjectを継承する。
 */
class GameItem(name: String, field: Field) : GameObject(name, field) {

    init {
        this.isThroughable = true
    }

    override fun display(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}