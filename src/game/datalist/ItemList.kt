package game.datalist

import game.item.GameCharacter
import game.mold.ItemMold

const val ITEM_POTION = 0
const val ITEM_MAGIC_POTION = 1
const val ITEM_BOOSTER = 2
const val ITEM_PROTECTOR= 3

val itemList = mapOf(
        ITEM_POTION to ItemMold(
                name = "ポーション",
                display = "💊",
                func = fun(gameCharactor: GameCharacter) {
//                    gameCharactor.abilityScore.hp.restore()
                }
        ),
        ITEM_POTION to ItemMold(
                name = "マジックポーション",
                display = "",
                func = fun(gameCharactor: GameCharacter) {
                }
        ),
        ITEM_BOOSTER to ItemMold(
                name = "ブースター",
                display = "",
                func = fun(gameCharactor: GameCharacter) {
                }
        )
)