package game.mold

import game.item.GameCharacter
import game.param.AbilityScore

/**
 * スキルを表す。
 * @property name スキル名
 * @property mpCost MP消費量
 * @property timeWaitPlus ゲームキャラクターの行動終了時にに追加で初期値(GameCharacter.TIME_WAIT_START)に加えて加算されるtimeWaitの値
 * @param action スキル実行時の動作を表す関数
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

        /**
         * 使用者と被使用者の能力値とスキルを指定して物理攻撃のダメージを計算する。
         * @property thisScore 使用者の能力値
         * @property targetScore 被使用者の能力値
         */
        fun calculatePhysicsDamage(skill: Skill, thisScore: AbilityScore, targetScore: AbilityScore) =
                thisScore.attack + skill.powerPlus - targetScore.defense / 2

        /**
         * 使用者と被使用者の能力値とスキルを指定して魔法攻撃のダメージを計算する。
         * @property thisScore 使用者の能力値
         * @property targetScore 被使用者の能力値
         */
        fun calculateMagicDamage(skill: Skill, thisScore: AbilityScore, targetScore: AbilityScore) =
                thisScore.magicAttack + skill.powerPlus - targetScore.magicDefense / 2
    }

}