package game.datalist

import game.mold.FloorInfo

val floorList = mapOf(
        1 to FloorInfo(
                arrayOf(ENEMY_PALIN),
                10,
                10
        ),
        3 to FloorInfo(
                arrayOf(ENEMY_PALIN, ENEMY_GOBLIN)
        ),
        6 to FloorInfo(
                arrayOf(ENEMY_PALIN, ENEMY_GOBLIN, ENEMY_COBOLT)
        ),
        11 to FloorInfo(
                arrayOf(ENEMY_PALIN, ENEMY_GOBLIN, ENEMY_COBOLT, ENEMY_KILLER_RAT),
                16,
                12
        ),
        16 to FloorInfo(
                arrayOf(ENEMY_GOBLIN, ENEMY_COBOLT, ENEMY_KILLER_RAT)
        ),
        21 to FloorInfo(
                arrayOf(ENEMY_GOBLIN, ENEMY_COBOLT, ENEMY_KILLER_RAT, ENEMY_HELLDOG)
        )
)