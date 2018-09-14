package game.field

import java.util.*

/**
 * フィールドマップのブロックに隣接する矢印のマップと各ブロックに1対1で対応するカウント変数を表す。
 * フィールドマップのスタート地点とゴール地点が移動可能かどうかを検証する。
 */
internal class FieldArrowMap(width: Int, height: Int, val field: Field) {

    /**
     * createArrowChainを実行するのに必要なパラメータ
     *  arrowGenerationQueueに格納される。
     */
    private class ArrowGenerationArgument(val x: Int, val y: Int, val arrowCount: Int, val arrow: Arrow, val prev: ArrowGenerationArgument? = null)
    
    /**
     * フィールド上のブロックに1対1で対応するカウントの2次元配列
     */
    private val fieldCountArray = Array<Array<Int?>>(height) { Array(width) { null } }

    /**
     * 左右の矢印の2次元配列
     */
    private val horizontalArrowArray = Array(height) { Array(width - 1) { Arrow.none } }

    /**
     * 上下の矢印の2次元配列
     */
    private val verticalArrowArray = Array(height - 1) { Array(width) { Arrow.none } }

    /**
     * createArrowChainを実行するのに必要なパラメータを格納するキュー
     */
    private val arrowGenerationQueue = LinkedList<ArrowGenerationArgument>()

    /**
     * スタート地点からの移動距離を表すカウント変数を取得する。
     *  @param x: Int フィールドのx位置
     *  @param y*Int フィールドのy位置
     *  @return Int? カウント変数
     */
    fun getArrowCount(x: Int, y: Int): Int? = fieldCountArray[y][x]

    /**
     * スタート地点からの移動距離を表すカウント変数を取得する。x,y位置が範囲外の場合はnullを返す。
     *  @param x: Int フィールドのx位置
     *  @param y*Int フィールドのy位置
     *  @return Int? カウント変数
     */
    private fun tryToGetArrowCount(x: Int, y: Int): Int? = try { fieldCountArray[y][x] } catch (e: ArrayIndexOutOfBoundsException) { null }

    /**
     * フィールド内の指定した位置の上下左右の隣にある矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param direction: Direction  矢印を取得するフィールドのxy位置からの方向
     * @return 上下左右の矢印またはnone
     */
    private fun getAdjacentArrow(x: Int, y: Int, direction: Direction): Arrow {

        return if (direction.isHorizonal()) {
            horizontalArrowArray[y][if (direction == Direction.left)  x - 1 else x]
        } else {
            verticalArrowArray[if (direction == Direction.top)  y - 1 else y][x]
        }
    }

    /**
     * 指定した位置の上下左右の隣にある矢印を取得する。位置が範囲外の場合はnullを返す。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param direction: Direction  矢印を取得するフィールドのxy位置からの方向
     * @return 上下左右の矢印またはnone
     */
    private fun tryToGetAdjacentArrow(x: Int, y: Int, direction: Direction): Arrow? {
        return try {
            getAdjacentArrow(x, y, direction)
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    /**
     * スタート地点からの移動距離を表すカウント変数を設定する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param count: Int カウント変数
     */
    private fun setArrowCount(x: Int, y: Int, count: Int?) { fieldCountArray[y][x] = count }

    /**
     * フィールド内の指定した位置の上下左右の隣の矢印をセットする。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param direction: Direction  矢印を設定するフィールドのxy位置からの方向
     * @param arrow: Arrow 設定する矢印の種類
     * @param ifNone: Boolean
     */
    private fun setAdjacentArrow(x: Int, y: Int, direction: Direction, arrow: Arrow, ifNone: Boolean = false): Boolean {

        if (direction == Direction.left || direction == Direction.right) {

            val arrowX = if (direction == Direction.left) x - 1 else x
            if (ifNone && horizontalArrowArray[y][arrowX] != Arrow.none) return false
            horizontalArrowArray[y][arrowX] = arrow

        } else {

            val arrowY = if (direction == Direction.top) y - 1 else y
            if (ifNone && verticalArrowArray[arrowY][x] != Arrow.none) return false
            verticalArrowArray[arrowY][x] = arrow

        }
        return true
    }

    /**
     * 指定した位置のカウントを削除して、上下左右の隣にある矢印もnoneにする。
     */
    private fun removeCountAndArrow(x: Int, y: Int) {
        fieldCountArray[y][x] = null
        tryToSetHorizontalArrow(x - 1, y, Arrow.none)
        tryToSetHorizontalArrow(x, y, Arrow.none)
        tryToSetVerticalArrow(x, y - 1, Arrow.none)
        tryToSetVerticalArrow(x, y, Arrow.none)
    }

    /**
     *
     */
    private fun getHorizontalArrow(x: Int, y: Int): Arrow {
        return horizontalArrowArray[y][x]
    }

    private fun setHorizontalArrow(x: Int, y: Int, arrow: Arrow) {
        horizontalArrowArray[y][x] = arrow
    }

    private fun getVerticalArrow(x: Int, y: Int): Arrow {
        return verticalArrowArray[y][x]
    }

    private fun setVerticalArrow(x: Int, y: Int, arrow: Arrow) {
        verticalArrowArray[y][x] = arrow
    }

    fun tryToGetHorizontalArrow(x: Int, y: Int): Arrow? {
        return try {
            getHorizontalArrow(x, y)
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    private fun tryToSetHorizontalArrow(x: Int, y: Int, arrow: Arrow): Boolean {
        return try {
            setHorizontalArrow(x, y, arrow)
            true
        } catch (e: ArrayIndexOutOfBoundsException) {
            false
        }
    }

    fun tryToGetVerticalArrow(x: Int, y: Int): Arrow? {
        return try {
            getVerticalArrow(x, y)
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    private fun tryToSetVerticalArrow(x: Int, y: Int, arrow: Arrow): Boolean {
        return try {
            setVerticalArrow(x, y, arrow)
            true
        } catch (e: ArrayIndexOutOfBoundsException) {
            false
        }
    }

    /**
     * 指定された位置から向かう矢印の本数を取得する。
     */
    fun getReferringNum(x: Int, y: Int): Int {
        var num = 0
        if (tryToGetHorizontalArrow(x - 1, y) == Arrow.left) num++
        if (tryToGetHorizontalArrow(x, y) == Arrow.right) num++
        if (tryToGetVerticalArrow(x, y - 1) == Arrow.top) num++
        if (tryToGetVerticalArrow(x, y) == Arrow.bottom) num++
        return num
    }

    /**
     * 指定された位置に向かう矢印の本数を取得する。
     */
    private fun getReferredNum(x: Int, y: Int): Int {
        var num = 0
        if (tryToGetHorizontalArrow(x - 1, y) == Arrow.right) num++
        if (tryToGetHorizontalArrow(x, y) == Arrow.left) num++
        if (tryToGetVerticalArrow(x, y - 1) == Arrow.bottom) num++
        if (tryToGetVerticalArrow(x, y) == Arrow.top) num++
        return num
    }

    fun regenerateBlockChain(x: Int, y: Int) {
        tryToGetArrowCount(x - 1, y)?.let { arrowCount -> arrowGenerationQueue.add(ArrowGenerationArgument(x - 1, y, arrowCount, Arrow.none)) }
        tryToGetArrowCount(x + 1, y)?.let { arrowCount -> arrowGenerationQueue.add(ArrowGenerationArgument(x + 1, y, arrowCount, Arrow.none)) }
        tryToGetArrowCount(x, y - 1)?.let { arrowCount -> arrowGenerationQueue.add(ArrowGenerationArgument(x, y - 1, arrowCount, Arrow.none)) }
        tryToGetArrowCount(x, y + 1)?.let { arrowCount -> arrowGenerationQueue.add(ArrowGenerationArgument(x, y + 1, arrowCount, Arrow.none)) }
        generateArrowMapInQueue()
    }

    /**
     * 指定された位置からの矢印マップを生成する。
     */
    fun generateArrowMap(x: Int, y: Int) {

        setArrowCount(x, y, count = 0)
        arrowGenerationQueue.push(ArrowGenerationArgument(x, y, 0, Arrow.none))

        generateArrowMapInQueue()
    }

    /**
     * キューに貯められたcreateNextArrowの実行待ちをすべて実行する。
     */
    fun generateArrowMapInQueue() {

        while (arrowGenerationQueue.size > 0) {

            val params = arrowGenerationQueue.first
            arrowGenerationQueue.removeFirst()
            generateArrowMapInQueueNext(params)
        }
    }

    private fun generateArrowMapInQueueNext(data : ArrowGenerationArgument): Boolean {
        val prev = data.prev
        val x = data.x
        val y = data.y

        //x,y位置が画面内にない場合、x,y位置にあるブロックが床でない場合は中断する。
        if (field.tryToGetFieldBlock(x, y)?.type != FieldBlockType.floor) { return false }

        //x,y位置のフィールドブロックの矢印カウントを取得する。
        val blockArrowCount = getArrowCount(x, y)

        if (prev != null) {
            //自身の現在のカウントより少ない矢印カウントのブロックにたどり着いた場合は中断する。
            if (data.arrowCount > blockArrowCount ?: Int.MAX_VALUE) return false
            //矢印を設定する。
            setAdjacentArrow(prev.x, prev.y, data.arrow.toDirection()!!, data.arrow, ifNone = false)
            //カウントが同じになった場合、
            if (data.arrowCount == blockArrowCount) return false

        } else if (blockArrowCount == null) {
            return false
        }
        //ブロックに現在のカウントを代入する。
        setArrowCount(x, y, data.arrowCount)
        generateMazeArrowToQueue(data, x, y)
        return true
    }

    private fun generateMazeArrowToQueue(data: ArrowGenerationArgument, x: Int, y: Int) {
        val nextArrowCount = data.arrowCount + 1
        arrowGenerationQueue.add(ArrowGenerationArgument(x - 1, y,     nextArrowCount, Arrow.left,   prev = data))
        arrowGenerationQueue.add(ArrowGenerationArgument(x + 1, y,     nextArrowCount, Arrow.right,  prev = data))
        arrowGenerationQueue.add(ArrowGenerationArgument(x,     y - 1, nextArrowCount, Arrow.top,    prev = data))
        arrowGenerationQueue.add(ArrowGenerationArgument(x,     y + 1, nextArrowCount, Arrow.bottom, prev = data))
    }

    /*
     * フィールド上の指定した位置から向かう矢印の鎖を削除する。
     * 指定した位置の上下左右の隣の矢印を調べ、指定した位置から外へ向かう矢印の場合は、削除を実行する。
     * @params x :Int フィールドのx位置
     * @params y :Int フィールドのy位置
     * @params isFirst  : Boolean 再起呼び出しでない場合はtrue、再起呼び出しの場合はfalse
     */
    fun removeArrowChain(x: Int, y: Int, isFirst: Boolean = false) {

        if (!isFirst && getReferredNum(x, y) > 0) {
            arrowGenerationQueue.push(ArrowGenerationArgument(x, y, getArrowCount(x, y)!!, Arrow.none))
            return
        }

        val nearLeftArrow = tryToGetAdjacentArrow(x, y, Direction.left)
        val nearRightArrow = tryToGetAdjacentArrow(x, y, Direction.right)
        val nearTopArrow = tryToGetAdjacentArrow(x, y, Direction.top)
        val nearToBottomArrow = tryToGetAdjacentArrow(x, y, Direction.bottom)

        //現在位置の矢印カウントを削除して上下左右の隣の矢印もnoneにする。
        removeCountAndArrow(x, y)

        if (nearLeftArrow == Arrow.left) removeArrowChain(x - 1, y)
        if (nearRightArrow == Arrow.right) removeArrowChain(x + 1, y)
        if (nearTopArrow == Arrow.top) removeArrowChain(x, y - 1)
        if (nearToBottomArrow == Arrow.bottom) removeArrowChain(x, y + 1)
    }

}