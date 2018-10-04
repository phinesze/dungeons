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
        var droppingExp: Long = 0,
        var elementAttack: ElementScore = ElementScore(),
        var elementDefense: ElementScore = ElementScore()
): Cloneable {

    companion object {
        /**
         * 指定した倍率から新しいAbilityMoldを作成する。
         */
        private fun generateAbilityMoldFrom(abilityMold: AbilityMold, mul: Double):AbilityMold {
            return AbilityMold(
                    (abilityMold.maxHp*mul).toInt(),
                    (abilityMold.maxMp*mul).toInt(),
                    (abilityMold.attack*mul).toInt(),
                    (abilityMold.defense*mul).toInt(),
                    (abilityMold.magicAttack*mul).toInt(),
                    (abilityMold.magicDefense*mul).toInt(),
                    (abilityMold.agility*mul).toInt(),
                    (abilityMold.droppingExp*mul.toInt())
            )
        }
    }

    /**
     * 各レベルの能力値の型を生成する
     * 基数^(レベル-1) + (レベル-1) * basicMultiplier
     * @param maxLevel レベルの最大値
     * @param 基数
     * @param basicMultiplier
     */
    fun toAbilityMap(maxLevel: Int = 99, 基数: Double = 1.0625, basicMultiplier: Double = 0.05): Map<Int, AbilityMold> {
        val result = mutableMapOf(1 to this)
        for (level in 2..maxLevel) {

            //倍率を設定
            val levelVal = (level - 1).toDouble()
            val multiplier = Math.pow(基数, levelVal) + levelVal * basicMultiplier

            result[level] = Companion.generateAbilityMoldFrom(this, multiplier)
        }
        return result.toMap()
    }

    override fun clone(): AbilityMold {
        return AbilityMold(maxHp, maxMp, attack, defense, magicAttack, magicDefense, agility)
    }
}