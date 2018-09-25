package game.datalist

import game.mold.EnemyMold
import game.param.AbilityMold
import game.param.AbilityScore

val enemyList = arrayOf(
        EnemyMold("ゴブリン",
                "§",
                AbilityMold(15, 0, 10, 10, 10, 10, 6)
        ),
        EnemyMold("コボルト",
                "Ξ",
                AbilityMold(20, 10, 12, 12, 4, 4, 10)
        )
)