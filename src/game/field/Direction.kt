package game.field

/**
 * 上下左右の方向を表す列挙体。フィールドの矢印レイヤー(FieldArrowMap)で使用される。
 * 「矢印がない」ことを表す"None"は方向には存在しない。
 * Directionが特定の方向を指定する場合に使用するのに対し、Arrowはフィールド上のブロックに隣接する矢印オブジェクトを表す。
 */
enum class Direction {

    Left, Right, Top, Bottom;

    companion object
}