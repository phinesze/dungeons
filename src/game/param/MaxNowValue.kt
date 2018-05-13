package game.param

class MaxNowValue(var max: Int, var now: Int = max) {

    //ダメージを与える。
    fun damage(value: Int): Int {
        val damageVal = if (value <= now) value else now
        now -= damageVal
        return damageVal
    }

    //回復する
    fun restore(value: Int): Int {
        val restoreVal = if (value <= max - now) value else max - now
        now += restoreVal
        return restoreVal
    }

    override fun toString(): String = "${now}/${max}"
}