package game.datalist

import game.item.GameCharacter
import game.mold.Skill

val skillList = arrayOf(
        Skill(
                name = "ファイア・α",
                action = fun(user: GameCharacter) {
                    user.actionLinear(moveX = -1, moveY = 0, action = fun(otherCharacter: GameCharacter) {
                        TODO("魔法を実装")
                    })
                },
                key = "fire-a",
                timeCost = 1000,
                mpCost = 5
        ),
        Skill(
                name = "コールド・α",
                action = fun(user: GameCharacter) {

                },
                key = "cold-a",
                timeCost = 1000,
                mpCost = 5
        )
)