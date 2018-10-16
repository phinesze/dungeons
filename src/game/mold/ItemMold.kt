package game.mold

import game.item.GameCharacter

/**
 * アイテムの種類を表す。
 * @property name アイテム名
 * @property action アイテム使用時の動作
 * @property display toStringで出力した際にオブジェクトの表示として表される文字
 */
class ItemMold(
        val name: String,
        val action: (GameCharacter) -> Unit,
        val display: String
)