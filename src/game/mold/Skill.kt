package game.mold

import game.item.GameCharacter
import game.param.AbilityScore

/**
 * スキルを表す。
 * @property name スキル名
 * @property mpCost MP消費量
 * @property timeWaitPlus ゲームキャラクターの行動終了時にに追加で初期値(GameCharacter.TIME_WAIT_START)に加えて加算されるtimeWaitの値
 * @param action
 */
class Skill(
        val name: String,
        val mpCost: Int,
        val timeWaitPlus: Int,
        val powerPlus: Int,
        val action: (Skill, GameCharacter, GameCharacter?) -> Unit,
        val calculator: (AbilityScore, AbilityScore, Skill) -> Int
) {

    companion object {

        val normalAttack = Skill(
                "",
                mpCost = 0,
                timeWaitPlus = 0,
                powerPlus = 0,
                action = { skill, user, target -> user.attackTarget(target!!, skill) }
        ) { userScore, targetScore, skill -> Skill.calculatePhysicsDamage(skill, userScore, targetScore) }

        private fun calculatePhysicsDamage(skill: Skill, thisScore: AbilityScore, targetScore: AbilityScore) =
                thisScore.attack + skill.powerPlus - targetScore.defense / 2

        fun calculateMagicDamage(skill: Skill, thisScore: AbilityScore, targetScore: AbilityScore) =
                thisScore.magicAttack + skill.powerPlus - targetScore.magicDefense / 2
    }

}