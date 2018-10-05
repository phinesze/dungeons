package game.param

/**
 * ゲームキャラクターの能力値となる値の内、2つ以上のキャラクタで共有される可能性のある能力値。
 * 現在HPなど現在値を含むAbilityScoreのプロパティとなる事で使用される。
 * @property maxHp 最大HP
 * @property maxMp 最大MP
 * @property attack 物理攻撃力
 * @property defense 物理防御力
 * @property magicAttack 魔法攻撃力
 * @property magicDefense 魔法防御力
 * @property
 */
class AbilityMold<T: Number>(
        var maxHp: T,
        var maxMp: T,
        var attack: T,
        var defense: T,
        var magicAttack: T,
        var magicDefense: T,
        var agility: T,
        var droppingExp: T
) : Cloneable {

    companion object {

        /**
         * 指定した倍率から新しいAbilityMoldを作成する。
         */
        private fun generateAbilityMoldFrom(
                baseMold: AbilityMold<Double>,
                mul: Double,
                expMul: Double = mul
        ): AbilityMold<Int> {
            return AbilityMold(
                    (baseMold.maxHp * mul).toInt(),
                    (baseMold.maxMp * mul).toInt(),
                    (baseMold.attack * mul).toInt(),
                    (baseMold.defense * mul).toInt(),
                    (baseMold.magicAttack * mul).toInt(),
                    (baseMold.magicDefense * mul).toInt(),
                    (baseMold.agility * mul).toInt(),
                    (baseMold.droppingExp * expMul).toInt()
            )
        }

        /**
         * 各レベルの能力値の型を生成する
         * 計算式 : cardinal^(level-1) + (level-1) * extraMul
         * @param maxLevel レベルの最大値
         * @param cardinal 能力値計算式の中の基数。この値にレベルに1を引いた値を累乗する。
         * @param extraMul 能力値計算式の中の乗数。この値にレベルに1を引いた値を掛ける。
         * @return 各レベルをキーとする能力値の型のハッシュマップ
         */
        fun toAbilityMap(
                baseMold: AbilityMold<Double>,
                maxLevel: Int = 99,
                cardinal: Double = 1.0625,
                extraMul: Double = 0.05
        ): Map<Int, AbilityMold<Int>> {

            //戻り値となるハッシュマップを生成
            val result = mutableMapOf<Int, AbilityMold<Int>>()

            //2～maxLevelまでの能力値を生成してハッシュマップに追加
            for (level in 1..maxLevel) {
                val levelMinus1 = (level - 1).toDouble()
                val abilityMoldMultiplier = Math.pow(cardinal, levelMinus1) + levelMinus1 * extraMul

                result[level] = generateAbilityMoldFrom(baseMold, abilityMoldMultiplier, 0.0)
            }
            return result.toMap()
        }
    }

    override fun clone(): AbilityMold<T> {
        return AbilityMold(maxHp, maxMp, attack, defense, magicAttack, magicDefense, agility, droppingExp)
    }
}

