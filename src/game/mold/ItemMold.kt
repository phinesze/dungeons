package game.mold

import game.NamableObject
import game.item.GameCharacter

/**
 * アイテムの種類を表す。
 */
class ItemMold(name: String, var action: (GameCharacter) -> Unit, var display: String) : NamableObject(name)