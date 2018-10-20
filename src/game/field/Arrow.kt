package game.field

/**
 * 上下左右と方向なしを表す矢印
 */
enum class Arrow {
    Left, Right, Top, Bottom, None;

    companion object {
        private val str = mapOf(
                Left to "←",
                Right to "→",
                Top to "↑",
                Bottom to "↓",
                None to "・"
        )

        private val arrowToDirection = mapOf(
                Left to Direction.Left,
                Right to Direction.Right,
                Top to Direction.Top,
                Bottom to Direction.Bottom
        )

    }

    /**
     * 矢印から方向に変換する。
     */
    fun toDirection(): Direction? {
        return arrowToDirection[this]
    }

    /**
     * 矢印を文字列として出力する。
     */
    override fun toString(): String {
        return str[this] ?: "  "
    }
}