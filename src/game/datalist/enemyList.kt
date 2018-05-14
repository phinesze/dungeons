package game.datalist

import game.mold.EnemyMold
import game.param.AbilityScore
import game.param.MaxNowValue

val enemyList = arrayOf(
        EnemyMold("ゴブリン",
                "§",
                AbilityScore(
                        MaxNowValue(10),
                        MaxNowValue(0),
                        10,
                        10,
                        10,
                        10,
                        6
                )
        ),
        EnemyMold("コボルト",
                "Ξ",
                AbilityScore(
                        MaxNowValue(13),
                        MaxNowValue(0),
                        12,
                        12,
                        4,
                        4,
                        10
                )
        )
)