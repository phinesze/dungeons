package game.mold

import game.item.GameCharacter
import game.param.AbilityScore

/**
 * スキルを表す。
 * @property name スキル名
 * @property mpCost MP消費量
 * @property timeCostPlus 待ち時間 通常行動時の1000に
 * @param action
 */
class Skill(
        val name: String,
        val mpCost: Int,
        val timeCostPlus: Int,
        val powerPlus: Int,
        val action: (Skill, GameCharacter, GameCharacter?) -> Unit,
        val calculator: (AbilityScore, AbilityScore, Skill) -> Int
) {

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