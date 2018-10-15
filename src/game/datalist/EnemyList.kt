package game.datalist

import game.mold.EnemyMold
import game.param.AbilityMold

const val ENEMY_PALIN = 0
const val ENEMY_GOBLIN = 1
const val ENEMY_COBOLT = 2
const val ENEMY_KILLER_RAT = 3
const val ENEMY_HELLDOG = 4

/**
 * 敵キャラクターのリストを定義する。
 */
val ENEMY_LIST = mapOf(
        ENEMY_PALIN to EnemyMold(
                name = "パリン",
                display = "Θ",
                abilityMap = AbilityMold.toAbilityMap(AbilityMold(
                        maxHp = 8.0,
                        maxMp = 0.0,
                        attack = 8.0,
                        defense = 10.0,
                        magicAttack = 10.0,
                        magicDefense = 10.0,
                        agility = 10.0,
                        droppingExp = 1.0
                ))
        ),
        ENEMY_GOBLIN to EnemyMold(
                name = "ゴブリン",
                display = "§",
                abilityMap = AbilityMold.toAbilityMap(AbilityMold(
                        maxHp = 15.0,
                        maxMp = 0.0,
                        attack = 10.0,
                        defense = 10.0,
                        magicAttack = 10.0,
                        magicDefense = 10.0,
                        agility = 10.0,
                        droppingExp = 2.0
                ))
        ),
        ENEMY_COBOLT to EnemyMold(
                name = "コボルト",
                display = "Ξ",
                abilityMap = AbilityMold.toAbilityMap(AbilityMold(
                        maxHp = 20.0,
                        maxMp = 10.0,
                        attack = 12.0,
                        defense = 12.0,
                        magicAttack = 4.0,
                        magicDefense = 4.0,
                        agility = 8.0,
                        droppingExp = 3.0
                ))
        ),
        ENEMY_KILLER_RAT to EnemyMold(
                name = "キラーラット",
                display = "Β",
                abilityMap = AbilityMold.toAbilityMap(AbilityMold(
                        maxHp = 8.0,
                        maxMp = 10.0,
                        attack = 10.0,
                        defense = 6.0,
                        magicAttack = 6.0,
                        magicDefense = 6.0,
                        agility = 25.0,
                        droppingExp = 3.0
                ))
        ),
        ENEMY_HELLDOG to EnemyMold(
                name = "ヘルドッグ",
                display = "×",
                abilityMap = AbilityMold.toAbilityMap(AbilityMold(
                        maxHp = 30.0,
                        maxMp = 0.0,
                        attack = 15.0,
                        defense = 10.0,
                        magicAttack = 10.0,
                        magicDefense = 10.0,
                        agility = 12.0,
                        droppingExp = 4.0
                ))
        )
)