package game.datalist

import game.mold.FloorInfo


val floorList = mapOf(
        1 to FloorInfo(
                enemyIdsParam = arrayOf(ENEMY_PALIN),
                enemyNumParam = 10,
                itemIdsParam = arrayOf(),
                itemNumParam = 0,
                fieldWidthParam = 12,
                fieldHeightParam = 10
        ),
        3 to FloorInfo(
                enemyIdsParam = arrayOf(ENEMY_PALIN, ENEMY_GOBLIN),
                itemIdsParam = arrayOf(ITEM_POTION),
                itemNumParam = 1
                ),
        6 to FloorInfo(
                arrayOf(ENEMY_PALIN, ENEMY_GOBLIN, ENEMY_COBOLT)
        ),
        11 to FloorInfo(
                enemyIdsParam = arrayOf(ENEMY_PALIN, ENEMY_GOBLIN, ENEMY_COBOLT, ENEMY_KILLER_RAT),
                fieldWidthParam = 16,
                fieldHeightParam = 12
        ),
        16 to FloorInfo(
                enemyIdsParam = arrayOf(ENEMY_GOBLIN, ENEMY_COBOLT, ENEMY_KILLER_RAT)
        ),
        21 to FloorInfo(
                enemyIdsParam = arrayOf(ENEMY_GOBLIN, ENEMY_COBOLT, ENEMY_KILLER_RAT, ENEMY_HELLDOG),
                enemyNumParam = 15,
                fieldWidthParam = 20,
                fieldHeightParam = 16
        )
)