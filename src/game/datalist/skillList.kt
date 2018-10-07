package game.datalist

import game.item.GameCharacter
import game.mold.Skill

const val SKILL_FIRE_A = 0
const val SKILL_COLD_A = 1

val skillList = mapOf(
        SKILL_FIRE_A to Skill(
                name = "ファイア・α",
                isMagic = true,
                mpCost = 5,
                timeCostPlus = 200,
                powerPlus = 5,
                keyInput = "fire-a",
                action = fun(skill: Skill, user: GameCharacter, _: GameCharacter?) {

                    val func = fun(target: GameCharacter) { user.attackTarget(target, skill) }

                    println("${user.name}の左右に炎の球が放たれる")
                    user.actionLinear(moveX = -1, moveY = 0, action = func)
                    user.actionLinear(moveX = 1, moveY = 0, action = func)
                },
                calculator = fun(userScore, targetScore, skill) = Skill.calculateMagicDamage(skill, userScore, targetScore)
        ),
        SKILL_COLD_A to Skill(
                name = "コールド・α",
                isMagic = true,
                mpCost = 5,
                timeCostPlus = 200,
                powerPlus = 0,
                keyInput = "cold-a",
                action = fun(skill: Skill, user: GameCharacter, dummy: GameCharacter?) {

                    val func = fun(target: GameCharacter) { user.attackTarget(target, skill) }

                    println("${user.name}の周りに氷の柱が巻き起こる")
                    user.actionLinear(moveX = 0, moveY = -1, action = func)
                    user.actionLinear(moveX = 0, moveY = 1, action = func)
                },
                calculator = fun(userScore, targetScore, skill) = Skill.calculateMagicDamage(skill, targetScore, userScore)
        )
)