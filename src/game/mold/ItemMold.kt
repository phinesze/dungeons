package game.mold

import game.item.GameCharacter

/**
 * アイテムの種類を表す。
 */
class ItemMold(
        val name: String,
        val action: (GameCharacter) -> Unit,
        val display: String
)