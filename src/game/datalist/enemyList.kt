package game.datalist

import game.mold.EnemyMold
import game.param.AbilityMold
import game.param.LevelAndExperience

const val ENEMY_PALIN = 0
const val ENEMY_GOBLIN = 1
const val ENEMY_COBOLT = 2
const val ENEMY_KILLER_RAT = 3
const val ENEMY_HELLDOG = 4

//val list = object (a = 0)

val enemyList = mapOf(
        ENEMY_PALIN to EnemyMold(
                name = "パリン",
                display = "Θ",
                abilityMap = AbilityMold(maxHp = 8, maxMp = 0, attack = 8, defense = 10, magicAttack = 10, magicDefense = 10, agility = 10, droppingExp = 1).toAbilityMap()
        ),
        ENEMY_GOBLIN to EnemyMold(
                name = "ゴブリン",
                display = "§",
                abilityMap = AbilityMold(maxHp = 15, maxMp = 0, attack = 10, defense = 10, magicAttack = 10, magicDefense = 10, agility = 10, droppingExp = 2).toAbilityMap()
        ),
        ENEMY_COBOLT to EnemyMold(
                name = "コボルト",
                display = "Ξ",
                abilityMap = AbilityMold(maxHp = 20, maxMp = 10, attack = 12, defense = 12, magicAttack = 4, magicDefense = 4, agility = 8, droppingExp = 3).toAbilityMap()
        ),
        ENEMY_KILLER_RAT to EnemyMold(
                name = "キラーラット",
                display = "Β",
                abilityMap = AbilityMold(maxHp = 8, maxMp = 10, attack = 10, defense = 6, magicAttack = 6, magicDefense = 6, agility = 25, droppingExp = 3).toAbilityMap()
        ),
        ENEMY_HELLDOG to EnemyMold(
                name = "ヘルドッグ",
                display = "×",
                abilityMap = AbilityMold(maxHp = 30, maxMp = 0, attack = 15, defense = 10, magicAttack = 10, magicDefense = 10, agility = 12, droppingExp = 4).toAbilityMap()
        )
)