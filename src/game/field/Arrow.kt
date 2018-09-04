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