package game.datalist

import game.mold.FloorInfo

val floorList = mapOf(
        1 to FloorInfo(
                enemyIds = arrayOf(ENEMY_PALIN),
                enemyNum = 10,
                fieldWidth = 12,
                fieldHeight = 10
        ),
        3 to FloorInfo(
                enemyIds = arrayOf(ENEMY_PALIN, ENEMY_GOBLIN),
                enemyNum = 10,
                fieldWidth = 12,
                fieldHeight = 10
                ),
        6 to FloorInfo(
                arrayOf(ENEMY_PALIN, ENEMY_GOBLIN, ENEMY_COBOLT)
        ),
        11 to FloorInfo(
                enemyIds = arrayOf(ENEMY_PALIN, ENEMY_GOBLIN, ENEMY_COBOLT, ENEMY_KILLER_RAT),
                enemyNum = 15,
                fieldWidth = 20,
                fieldHeight = 15
        ),
        16 to FloorInfo(
                enemyIds = arrayOf(ENEMY_GOBLIN, ENEMY_COBOLT, ENEMY_KILLER_RAT),
                enemyNum = 15,
                fieldWidth = 20,
                fieldHeight = 15
        ),
        21 to FloorInfo(
                enemyIds = arrayOf(ENEMY_GOBLIN, ENEMY_COBOLT, ENEMY_KILLER_RAT, ENEMY_HELLDOG),
                enemyNum = 15,
                fieldWidth = 16,
                fieldHeight = 16
        )
)