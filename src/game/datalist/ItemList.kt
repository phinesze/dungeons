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
                action = fun(gameCharacter: GameCharacter) {
                    val restoreVal = gameCharacter.abilityScore.hp.restoreRatio(0.5)
                    println("${gameCharacter.name}はHPを${restoreVal}回復した！！")
                },
                key = "q",
                display = "💊"
        ),
        ITEM_MAGIC_POTION to ItemMold(
                name = "マジックポーション",
                action = fun(gameCharacter: GameCharacter) {
                    val restoreVal = gameCharacter.abilityScore.mp.restoreRatio(0.5)
                    println("${gameCharacter.name}はMPを${restoreVal}回復した！！")
                },
                key = "w",
                display = "Ｍ"
        ),
        ITEM_BOOSTER to ItemMold(
                name = "ブースター",
                action = fun(gameCharacter: GameCharacter) {
                },
                key = "e",
                display = "Ｂ"
        ),
        ITEM_PROTECTOR to ItemMold(
                name = "プロテクター",
                action = fun(gameCharacter: GameCharacter) {
                },
                key = "r",
                display = "Ｐ"
        )
)