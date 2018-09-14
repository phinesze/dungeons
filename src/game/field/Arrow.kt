package game.field

/**
 * 上下左右と方向なしを表す矢印
 */
enum class Arrow {
    left, right, top, bottom, none;

    companion object {
        private val str = mapOf(
                left to "←",
                right to "→",
                top to "↑",
                bottom to "↓",
                none to "・"
        )

        private val arrowToDirection = mapOf(
                left to Direction.left,
                right to Direction.right,
                top to Direction.top,
                bottom to Direction.bottom
        )

    }

    /**
     * 方向が水平方向(左右の方向)か否かの値を返す。
     * @return 水平(左右)である場合true、そうでない場合はfalseを返す。
     */
    fun isHorizontal(): Boolean = this == Arrow.left || this == Arrow.right

    /**
     * 方向が垂直方向(上下の方向)か否かの値を返す。
     * @return 垂直(上下)である場合true、そうでない場合はfalseを返す。
     */
    fun isVertical(): Boolean = this == Arrow.top || this == Arrow.bottom

    /**
     *
     */
    fun toDirection(): Direction? {
        return arrowToDirection[this]
    }

    /**
     * 
     */
    override fun toString(): String {
        return str[this] ?: "  "
    }
}