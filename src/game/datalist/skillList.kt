package game.datalist

import game.item.GameCharacter
import game.mold.Skill

const val SKILL_NORMAL_ATTACK = -1
const val SKILL_FIRE_A = 0
const val SKILL_COLD_A = 1

val skillList = mapOf(
        SKILL_NORMAL_ATTACK to Skill(
                "",
                isMagic = false,
                mpCost = 0,
                power = 0,
                timeCost = 1000,
                keyInput = "",
                action = fun(skill: Skill, user: GameCharacter, target: GameCharacter?) {
                    user.attackTarget(target!!, skill)
                },
                calculator = fun(userScore, targetScore, skill) = Skill.calculatePhysicsDamage(targetScore, userScore, skill)
        ),
        SKILL_FIRE_A to Skill(
                name = "ファイア・α",
                isMagic = true,
                mpCost = 5,
                timeCost = 1000,
                power = 0,
                keyInput = "fire-a",
                action = fun(skill: Skill, user: GameCharacter, dummy: GameCharacter?) {

                    val func = fun(target: GameCharacter) {
                        user.attackTarget(target, skill)
                    }

                    println("${user.name}の周りに炎の柱が巻き起こる")
                    user.actionLinear(moveX = -1, moveY = 0, action = func)
                    user.actionLinear(moveX = 1, moveY = 0, action = func)
                },
                calculator = fun(userScore, targetScore, skill) = Skill.calculateMagicDamage(skill, targetScore, userScore)
        ),
        SKILL_COLD_A to Skill(
                name = "コールド・α",
                isMagic = true,
                mpCost = 5,
                timeCost = 1000,
                power = 0,
                keyInput = "cold-a",
                action = fun(skill: Skill, user: GameCharacter, dummy: GameCharacter?) {

                },
                calculator = fun(userScore, targetScore, skill) = Skill.calculateMagicDamage(skill, targetScore, userScore)
        )
)