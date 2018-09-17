package game.field

import game.item.GameObject

/**
 * フィールドを構成するブロック
 * block object that Field consist of
 */
class FieldBlock(var type: FieldBlockType) {
    val gameObjects: MutableList<GameObject> = mutableListOf()

    /**
     * ブロックの上にオブジェクトが有るならば一番上のオブジェクトを、無いならばブロックの文字列を返す。
     */
    override fun toString(): String {
        if (gameObjects.size > 0) {
            for (i in gameObjects.size - 1 downTo 0) {
                val gameObject = gameObjects[i]
                return gameObject.display()
            }
        }
        return type.toString()
    }
}