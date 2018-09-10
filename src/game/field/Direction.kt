package game.field

/**
 * 上下左右の方向を表す列挙体。フィールドの矢印レイヤー(FieldArrowMap)で使用される。
 * 「矢印がない」ことを表す"none"は方向には存在しない。
 * Directionが特定の方向を指定する場合に使用するのに対し、Arrowはフィールド上のブロックに隣接する矢印オブジェクトを表す。
 */
enum class Direction {

    left, right, top, bottom;

    companion object {
        private val directionToArrow = mapOf(
                left to Arrow.left,
                right to Arrow.right,
                top to Arrow.top,
                bottom to Arrow.bottom
        )
    }

    /**
     * 方向が水平方向(左右の方向)か否かの値を返す。
     * @return 水平(左右)である場合true、そうでない場合はfalseを返す。
     */
    fun isHorizonal(): Boolean = this == Direction.left || this == Direction.right

    /**
     * 方向が垂直方向(上下の方向)か否かの値を返す。
     * @return 垂直(上下)である場合true、そうでない場合はfalseを返す。
     */
    fun isVertical(): Boolean = this == Direction.top || this == Direction.bottom

    /**
     * 矢印オブジェクト(@see Arrow)に変換する
     */
    fun toArrow(): Arrow {
        return directionToArrow[this]!!
    }
}