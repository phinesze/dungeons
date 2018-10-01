package game.param

/**
 * ゲームキャラクターの能力値となる値の内、2つ以上のキャラクタで共有される可能性のある能力値。
 * 現在HPなど現在値を含むAbilityScoreのプロパティとなる事で使用される。
 */
class AbilityMold(
        var maxHp: Int,
        var maxMp: Int,
        var attack: Int,
        var defense: Int,
        var magicAttack: Int,
        var magicDefense: Int,
        var agility: Int,
        var elementAttack: ElementScore = ElementScore(),
        var elementDefense: ElementScore = ElementScore()
): Cloneable {

    companion object {
        /**
         * 指定した倍率から新しいAbilityMoldを作成する。
         */
        private fun generateAbilityMoldFrom(abilityMold: AbilityMold, 倍率: Double):AbilityMold {
            return AbilityMold(
                    (abilityMold.maxHp*倍率).toInt(),
                    (abilityMold.maxMp*倍率).toInt(),
                    (abilityMold.attack*倍率).toInt(),
                    (abilityMold.defense*倍率).toInt(),
                    (abilityMold.magicAttack*倍率).toInt(),
                    (abilityMold.magicDefense*倍率).toInt(),
                    (abilityMold.agility*倍率).toInt()
            )
        }
    }

    /**
     *
     */
    fun toAbilityMap(
            maxLevel: Int = 99,
            基数: Double = 1.0625,
            basicMultiplier: Double = 0.05
    ): Map<Int, AbilityMold> {
        val result = mutableMapOf(1 to this)
        for (level in 2..maxLevel) {

            //倍率を設定
            val multiplier =
                    Math.pow(基数, (level - 1).toDouble()) +
                            (level - 1) * basicMultiplier

            result[level] = Companion.generateAbilityMoldFrom(this, multiplier)
        }
        return result.toMap()
    }

    override fun clone(): AbilityMold {
        return AbilityMold(maxHp, maxMp, attack, defense, magicAttack, magicDefense, agility)
    }
}