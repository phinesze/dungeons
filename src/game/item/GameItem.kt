package game.item

/**
 *  フィールド上で拾えるアイテムを表すクラス。GameObjectを継承する。
 */
class GameItem(name: String) : GameObject(name) {

    init {
        this.isThroughable = true
    }

    override fun display(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}