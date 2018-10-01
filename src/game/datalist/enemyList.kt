package game.datalist

import game.mold.EnemyMold
import game.param.AbilityMold
import game.param.LevelAndExperience

val enemyList = arrayOf(
        EnemyMold(name = "パリン", display = "Θ", abilityMap = AbilityMold(maxHp = 8, maxMp = 0, attack = 8, defense = 10, magicAttack = 10, magicDefense = 10, agility = 10).toAbilityMap(), levelAndExp = LevelAndExperience(level = 1, experience = 2)),
        EnemyMold(name = "ゴブリン", display = "§", abilityMap = AbilityMold(maxHp = 15, maxMp = 0, attack = 10, defense = 10, magicAttack = 10, magicDefense = 10, agility = 10).toAbilityMap(), levelAndExp = LevelAndExperience(level = 1, experience = 1)),
        EnemyMold(name = "コボルト", display = "Ξ", abilityMap = AbilityMold(maxHp = 20, maxMp = 10, attack = 12, defense = 12, magicAttack = 4, magicDefense = 4, agility = 8).toAbilityMap(), levelAndExp = LevelAndExperience(level = 1, experience = 3))
)