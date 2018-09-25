package game.param

/**
 * 経験値とレベルを表現する。
 * @param 仮数 仮数
 * @param 基数
 * @parm level = 1
 *
 */
class LevelAndExperience(
        仮数: Long = 10,
        基数: Double = 1.125,
        level: Int = 1,
        maxLevel: Int = 99,
        experience: Long = 0
) {

    /**
     * 現在のレベルを表す1から始まる値
     */
    var level: Int = level
        private set(levelValue) { if (levelValue > this.maxLevel) throw IllegalArgumentException(); field = levelValue }

    /**
     * 最大レベル
     */
    var maxLevel = maxLevel
        set(maxLevelValue) { if (this.level > maxLevelValue) throw IllegalArgumentException(); field = maxLevelValue }

    /**
     * 現在までの累積経験値
     */
    var experience: Long = 0
        private set

    /**
     * 各時点でのレベルレベルをキーとしたレベルが1上がるごとに必要な累積経験値のハッシュマップ
     */
    private val nextExpMap = mutableMapOf<Int, Long>(1 to 仮数)

    /**
     * 次のレベルになるのに必要な累積経験値
     */
    private val requiredExp = this.nextExpMap[this.level]

    init {
        initNextExpMap(仮数, 基数)
    }

    /**
     * nextExpMapを初期化する。
     */
    private fun initNextExpMap(仮数: Long, 基数: Double) {
        for (level in 2..99) {
            nextExpMap[level] = nextExpMap[level - 1]!! + (仮数 * Math.pow(基数, level.toDouble())).toLong()
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

        while (
                this.level < this.maxLevel &&
                this.experience >= requiredExp!!
        ) {
            this.level++
            raisedLevel++
        }

        return raisedLevel
    }
}