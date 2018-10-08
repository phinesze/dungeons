package game.param

/**
 * HP、MPのような最大値と現在値のある値を表現する。
 */
class MaxNowValue(var max: Int, var now: Int = max) {

    /**
     * 現在値を減少させる。
     */
    fun damage(value: Int): Int {
        val damageVal = if (value > now) now else if (value < 0) 0 else value
        now -= damageVal
        return damageVal
    }

    /**
     * 現在値を回復させる。
     */
    private fun restore(value: Int): Int {
        val restoreVal = if (value <= max - now) value else max - now
        now += restoreVal
        return restoreVal
    }

    /**
     * 最大値に対する割合を指定して回復させる。
     */
    fun restoreRatio(ratio: Double): Int {
        return restore((max * ratio).toInt())
    }

    override fun toString(): String = "$now/$max"
}