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
        var timeCost: Int,
        val power: Int,
        var keyInput: String,
        var action: (Skill, GameCharacter, GameCharacter?) -> Unit,
        var calculator: (AbilityScore, AbilityScore, Skill) -> Int
) : NamableObject(name) {

    companion object {
        fun calculatePhysicsDamage(targetScore: AbilityScore, thisScore: AbilityScore, skill: Skill) =
                thisScore.attack + skill.power - targetScore.defense / 2

        fun calculateMagicDamage(skill: Skill, thisScore: AbilityScore, targetScore: AbilityScore) =
                thisScore.magicAttack + skill.power - targetScore.magicDefense / 2
    }

}