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
        var mpCost: Int,
        var timeCostPlus: Int,
        val powerPlus: Int,
        var action: (Skill, GameCharacter, GameCharacter?) -> Unit,
        var calculator: (AbilityScore, AbilityScore, Skill) -> Int
) : NamableObject(name) {

    companion object {

        val normalAttack = Skill(
                "",
                mpCost = 0,
                timeCostPlus = 1000,
                powerPlus = 0,
                action = fun(skill: Skill, user: GameCharacter, target: GameCharacter?) {
                    user.attackTarget(target!!, skill)
                },
                calculator = fun(userScore, targetScore, skill) = Skill.calculatePhysicsDamage(skill, userScore, targetScore)
        )

        private fun calculatePhysicsDamage(skill: Skill, thisScore: AbilityScore, targetScore: AbilityScore) =
                thisScore.attack + skill.powerPlus - targetScore.defense / 2

        fun calculateMagicDamage(skill: Skill, thisScore: AbilityScore, targetScore: AbilityScore) =
                thisScore.magicAttack + skill.powerPlus - targetScore.magicDefense / 2
    }

}