package game.param

/**
 * プレイヤーキャラクターの経験値とレベルを表現する。LevelAndExperienceを継承する。
 * 初期レベル・最大レベル・初期累積経験値と累積経験値のハッシュマップを作成する際の仮数と基数を指定する。
 *
 * レベルが1の時点での必要経験値は仮数の値そのままの数値となる。
 * 各レベルの必要累積経験値は、前のレベルまで累積した経験値に 仮数 × 基数^(現在のレベル - 1) を足した値となる。
 *
 * @param level レベルの初期値
 * @param maxLevel レベルの最大値
 * @param experience 累積経験値の初期値
 * @param significand 累積経験値のハッシュマップを作成する際の仮数
 * @param cardinal 累積経験値のハッシュマップを作成する際の基数
 */
class PlayerLevelAndExperience(
        level: Int = 1,
        maxLevel: Int = 99,
        experience: Long = 0,
        significand: Long = 10,
        cardinal: Double = 1.125
) : LevelAndExperience(level, experience) {

    /**
     * 最大レベル
     */
    var maxLevel: Int = maxLevel
        set(maxLevelValue) { if (this.level > maxLevelValue) throw IllegalArgumentException(); field = maxLevelValue }

    /**
     * 各時点でのレベルをキーとしたレベルが1上がるごとに必要な累積経験値のハッシュマップ
     */
    private val nextExpMap: MutableMap<Int, Long> = mutableMapOf(1 to significand)

    /**
     * 次のレベルになるのに必要な累積経験値のハッシュマップ
     * 各レベルの値をキーとして、そのレベルから1レベル上昇するまでの累積経験値を値とする。
     */
    private val requiredExp: Long?
        get() = this.nextExpMap[this.level]

    init {
        initNextExpMap(significand, cardinal)
    }

    /**
     * 累積経験値のハッシュマップを初期化する。
     */
    private fun initNextExpMap(significand: Long, cardinal: Double) {
        for (level in 2..99) {
            nextExpMap!![level] =
                    nextExpMap[level - 1]!! + (significand * Math.pow(cardinal, level.toDouble())).toLong()
        }
    }

    /**
     * 経験値を追加する。
     * @param exp 追加する値
     * @return この動作でのレベルが上昇した数
     */
    fun addExperience(exp :Long): Int{

        var raisedLevel: Int = 0
        this.experience += exp

        while (this.level < this.maxLevel && this.experience >= requiredExp!!) {
            this.level++
            raisedLevel++
        }

        return raisedLevel
    }
}