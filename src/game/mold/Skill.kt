package game.mold

import game.NamableObject
import game.item.GameCharacter
import game.param.AbilityScore

/**
 * スキルを表す。
 * @param name スキル名
 * @param action
 */
class Skill(
        name: String,
        var isMagic: Boolean,
        var mpCost: Int,
        var timeCostPlus: Int,
        val powerPlus: Int,
        var keyInput: String,
        var action: (Skill, GameCharacter, GameCharacter?) -> Unit,
        var calculator: (AbilityScore, AbilityScore, Skill) -> Int
) : NamableObject(name) {

    companion object {

        val normalAttack = Skill(
                "",
                isMagic = false,
                mpCost = 0,
                powerPlus = 0,
                timeCostPlus = 1000,
                keyInput = "",
                action = fun(skill: Skill, user: GameCharacter, target: GameCharacter?) {
                    user.attackTarget(target!!, skill)
                },
                calculator = fun(userScore, targetScore, skill) = Skill.calculatePhysicsDamage(skill, userScore, targetScore)
        )

        fun calculatePhysicsDamage(skill: Skill, thisScore: AbilityScore, targetScore: AbilityScore) =
                thisScore.attack + skill.powerPlus - targetScore.defense / 2

        fun calculateMagicDamage(skill: Skill, thisScore: AbilityScore, targetScore: AbilityScore) =
                thisScore.magicAttack + skill.powerPlus - targetScore.magicDefense / 2
    }

}