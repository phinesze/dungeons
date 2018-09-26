package game.param

/**
 * プレイヤーキャラクターの経験値とレベルを表現する。LevelAndExperienceを継承する。
 * プレイヤー用の場合は、初期レベル・最大レベル・初期経験値を指定する。
 *
 * @param level プレイヤーの場合は初期レベル、敵キャラクターの場合は敵のレベル
 * @param maxLevel レベル最大値、敵キャラクターの場合は設定しない
 * @param experience プレイヤーの場合は初期累積経験値、敵の場合は倒した際にプレイヤーが獲得する経験値
 * @param 仮数 仮数
 * @param 基数 基数
 */
class PlayerLevelAndExperience(
        level: Int = 1,
        maxLevel: Int = 99,
        experience: Long = 0,
        仮数: Long = 10,
        基数: Double = 1.125
) : LevelAndExperience(level, experience) {

    /**
     * 最大レベル
     */
    var maxLevel: Int = maxLevel
        set(maxLevelValue) { if (this.level > maxLevelValue) throw IllegalArgumentException(); field = maxLevelValue }

    /**
     * 各時点でのレベルレベルをキーとしたレベルが1上がるごとに必要な累積経験値のハッシュマップ
     */
    private val nextExpMap: MutableMap<Int, Long> = mutableMapOf(1 to 仮数)

    /**
     * 次のレベルになるのに必要な累積経験値のハッシュマップ
     * 各レベルの値をキーとして、そのレベルから1レベル上昇するまでの累積経験値を値とする。
     */
    private val requiredExp: Long? = this.nextExpMap[this.level]

    init {
        initNextExpMap(仮数, 基数)
    }

    /**
     * （プレイヤー用の値の場合）nextExpMapを初期化する。
     */
    private fun initNextExpMap(仮数: Long, 基数: Double) {
        for (level in 2..99) {
            nextExpMap!![level] = nextExpMap[level - 1]!! + (仮数 * Math.pow(基数, level.toDouble())).toLong()
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