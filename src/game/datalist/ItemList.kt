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
                action = fun(gameCharactor: GameCharacter) {
                    gameCharactor.abilityScore.hp.restoreRatio(0.5)
                },
                key = "q",
                display = "💊"
        ),
        ITEM_POTION to ItemMold(
                name = "マジックポーション",
                action = fun(gameCharacter: GameCharacter) {
                    gameCharacter.abilityScore.hp.restoreRatio(0.5)
                },
                key = "w",
                display = "Ｍ"
        ),
        ITEM_BOOSTER to ItemMold(
                name = "ブースター",
                action = fun(gameCharactor: GameCharacter) {
                },
                key = "e",
                display = "Ｂ"
        ),
        ITEM_PROTECTOR to ItemMold(
                name = "プロテクター",
                action = fun(gameCharactor: GameCharacter) {
                },
                key = "r",
                display = "Ｐ"
        )
)