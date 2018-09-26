package game.datalist

import game.mold.EnemyMold
import game.param.AbilityMold
import game.param.LevelAndExperience

val enemyList = arrayOf(
        EnemyMold(name = "ゴブリン", display = "§", abilityMold = AbilityMold(maxHp = 15, maxMp = 0, attack = 10, defense = 10, magicAttack = 10, magicDefense = 10, agility = 6), levelAndExp = LevelAndExperience(level = 1, experience = 1)),
        EnemyMold(name = "コボルト", display = "Ξ", abilityMold = AbilityMold(maxHp = 20, maxMp = 10, attack = 12, defense = 12, magicAttack = 4, magicDefense = 4, agility = 10), levelAndExp = LevelAndExperience(level = 1, experience = 3))
)