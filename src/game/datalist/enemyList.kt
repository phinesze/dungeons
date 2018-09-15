package game.datalist

import game.mold.EnemyMold
import game.param.AbilityMold
import game.param.AbilityScore

val enemyList = arrayOf(
        EnemyMold("ゴブリン",
                "§",
                AbilityScore(10, 0, AbilityMold(10, 10, 10, 10, 6))
        ),
        EnemyMold("コボルト",
                "Ξ",
                AbilityScore(13, 0, AbilityMold(12, 12, 4, 4, 10))
        )
)