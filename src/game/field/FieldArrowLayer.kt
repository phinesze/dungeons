package game.field

class FieldArrowLayer(val width: Int, val height: Int) {

    /**
     * フィールド上のブロックに1対1で対応するカウントの2次元配列
     */
    private val fieldCountArray = Array<Array<Int?>>(height,init = {i -> Array(width, {i -> null})})

    /**
     * 左右の矢印の2次元配列
     */
    private val horizonalArrowArray = Array<Array<Arrow>>(height, init = {i -> Array(width - 1, {i -> Arrow.none})})

    /**
     * 上下の矢印の2次元配列
     */
    private val verticalArrowArray = Array<Array<Arrow>>(height - 1, init = {i -> Array(width, {i -> Arrow.none})})

    /**
     * カウントを取得する。
     */
    fun getArrowCount(x: Int, y: Int): Int? = fieldCountArray[y][x]

    /**
     * カウントを取得する。x,y位置が範囲外の場合はnullを返す。
     */
    fun tryToGetArrowCount(x: Int, y: Int): Int? = try { fieldCountArray[y][x] } catch (e: ArrayIndexOutOfBoundsException) { null }

    /**
     * カウントを設定する。(非推奨)
     */
    fun setArrowCount(x: Int, y: Int, count: Int?) {
        fieldCountArray[y][x] = count
    }

    /**
     * 指定した位置の上下左右の隣にある矢印を取得する。
     * @return 上下左右の矢印またはnone
     */
    fun getArrow(x: Int, y: Int, direction: Direction): Arrow {

        if (direction == Direction.left || direction == Direction.right) {
            return horizonalArrowArray[y][if (direction == Direction.left)  x - 1 else x]
        } else {
            return verticalArrowArray[if (direction == Direction.top)  y - 1 else y][x]
        }
    }

    /**
     * 指定した位置の上下左右の隣の矢印をセットする。
     */
    fun setArrow(x: Int, y: Int, direction: Direction, arrow: Arrow, ifNone: Boolean = false): Boolean {

        if (direction == Direction.left || direction == Direction.right) {

            val arrowX = if (direction == Direction.left) x - 1 else x
            if (ifNone && horizonalArrowArray[y][arrowX] != Arrow.none) return false
            horizonalArrowArray[y][arrowX] = arrow

        } else {

            val arrowY = if (direction == Direction.top) y - 1 else y
            if (ifNone && verticalArrowArray[arrowY][x] != Arrow.none) return false
            verticalArrowArray[arrowY][x] = arrow

        }
        return true
    }

    /**
     * 指定した位置の上下左右の隣にある矢印を取得する。位置が範囲外の場合はnullを返す。
     * @return 上下左右の矢印またはnone
     */
    fun tryToGetArrow(x: Int, y: Int, direction: Direction): Arrow? {
        try { return getArrow(x, y, direction) } catch (e: ArrayIndexOutOfBoundsException) { return null }
    }


    fun tryToSetArrow(x: Int, y: Int, direction: Direction, arrow: Arrow, ifNone: Boolean = false):Boolean {
        try { return setArrow(x, y, direction, arrow, ifNone) } catch (e: ArrayIndexOutOfBoundsException) { return false }
    }



    /**
     * 指定した位置のカウントを削除して、上下左右の隣にある矢印もnoneにする。
     */
    fun removeCountAndArrow(x: Int, y: Int) {
        fieldCountArray[y][x] = null
        tryToSetHorizonalArrow(x - 1, y, Arrow.none)
        tryToSetHorizonalArrow(x, y, Arrow.none)
        tryToSetVerticalArrow(x, y - 1, Arrow.none)
        tryToSetVerticalArrow(x, y, Arrow.none)
    }

    fun getHorizonalArrow(x: Int, y: Int): Arrow {
        return horizonalArrowArray[y][x]
    }

    fun setHorizonalArrow(x: Int, y: Int, arrow: Arrow) {
        horizonalArrowArray[y][x] = arrow
    }

    fun getVerticalArrow(x: Int, y: Int): Arrow {
        return verticalArrowArray[y][x]
    }

    fun setVerticalArrow(x: Int, y: Int, arrow: Arrow) {
        verticalArrowArray[y][x] = arrow
    }

    fun tryToGetHorizonalArrow(x: Int, y: Int): Arrow? {
        try {
            return getHorizonalArrow(x, y)
        } catch (e: ArrayIndexOutOfBoundsException) {
            return null
        }
    }

    fun tryToSetHorizonalArrow(x: Int, y: Int, arrow: Arrow): Boolean {
        try {
            setHorizonalArrow(x, y, arrow)
            return true
        } catch (e: ArrayIndexOutOfBoundsException) {
            return false
        }
    }

    fun tryToGetVerticalArrow(x: Int, y: Int): Arrow? {
        try {
            return getVerticalArrow(x, y)
        } catch (e: ArrayIndexOutOfBoundsException) {
            return null
        }
    }

    fun tryToSetVerticalArrow(x: Int, y: Int, arrow: Arrow): Boolean {
        try {
            setVerticalArrow(x, y, arrow)
            return true
        } catch (e: ArrayIndexOutOfBoundsException) {
            return false
        }
    }

    /**
     * 指定された位置から向かう矢印の本数を取得する。
     */
    fun getReferingNum(x: Int, y: Int): Int {
        var num = 0
        if (tryToGetHorizonalArrow(x - 1, y) == Arrow.left) num++
        if (tryToGetHorizonalArrow(x, y) == Arrow.right) num++
        if (tryToGetVerticalArrow(x, y - 1) == Arrow.top) num++
        if (tryToGetVerticalArrow(x, y) == Arrow.bottom) num++
        return num
    }

    /**
     * 指定された位置に向かう矢印の本数を取得する。
     */
    fun getReferedNum(x: Int, y: Int): Int {
        var num = 0
        if (tryToGetHorizonalArrow(x - 1, y) == Arrow.right) num++
        if (tryToGetHorizonalArrow(x, y) == Arrow.left) num++
        if (tryToGetVerticalArrow(x, y - 1) == Arrow.bottom) num++
        if (tryToGetVerticalArrow(x, y) == Arrow.top) num++
        return num
    }
}