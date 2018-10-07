package game.datalist

import game.item.GameCharacter
import game.mold.Skill

val skillList = arrayOf(
        Skill(
                name = "ファイア・α",
                action = fun(user: GameCharacter) {

                    println("${user.name}の周りに炎の柱が巻き起こる")
                    user.actionLinear(moveX = -1, moveY = 0, action = fun(target: GameCharacter) {
                        val damage = user.attackTarget(target, isMagic = true)
                    })
                },
                keyInput = "fire-a",
                timeCost = 1000,
                mpCost = 5
        ),
        Skill(
                name = "コールド・α",
                action = fun(user: GameCharacter) {

                },
                keyInput = "cold-a",
                timeCost = 1000,
                mpCost = 5
        )
)