package game.field

import game.param.Position

/**
 * 上下左右の方向を表す列挙体。フィールドの矢印レイヤー(FieldArrowMap)で使用される。
 * 「矢印がない」ことを表す"None"は方向には存在しない。
 * Directionが特定の方向を指定する場合に使用するのに対し、Arrowはフィールド上のブロックに隣接する矢印オブジェクトを表す。
 */
enum class Direction {

    Left, Right, Top, Bottom;

    companion object {

        /**
         * 上下左右隣のフィールドブロックまたは距離カウントを調べる場合の現在のブロックとの差を位置情報のハッシュマップとして表す。
         */
        val positionMoves = mapOf(
                Left to Position(-1, 0),
                Right to Position(1, 0),
                Top to Position(0, -1),
                Bottom to Position(0, 1)
        )

        private val directionToArrow = mapOf(
                Left to Arrow.Left,
                Right to Arrow.Right,
                Top to Arrow.Top,
                Bottom to Arrow.Bottom
        )
    }

    /**
     * 矢印から方向に変換する。
     */
    fun toArrow(): Arrow? {
        return directionToArrow[this]
    }
}