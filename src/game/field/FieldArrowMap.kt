package game.field

import java.util.*

/**
 * フィールドマップのブロックに隣接する矢印のマップと各ブロックに1対1で対応するカウント変数を表す。
 * フィールドマップのスタート地点とゴール地点が移動可能かどうかを検証する。
 *
 * generateArrowMapを使用する。
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
     * 指定された位置からの矢印マップを生成する。
     */
    fun generateArrowMap(x: Int, y: Int) {

        setArrowCount(x, y, count = 0)
        arrowGenerationQueue.push(ArrowGenerationArgument(x, y, 0, Arrow.none))

        generateArrowMapInQueue()
    }


    fun regenerateBlockChain(x: Int, y: Int) {
        tryToGetArrowCount(x - 1, y)?.let { arrowCount -> arrowGenerationQueue.add(ArrowGenerationArgument(x - 1, y, arrowCount, Arrow.none)) }
        tryToGetArrowCount(x + 1, y)?.let { arrowCount -> arrowGenerationQueue.add(ArrowGenerationArgument(x + 1, y, arrowCount, Arrow.none)) }
        tryToGetArrowCount(x, y - 1)?.let { arrowCount -> arrowGenerationQueue.add(ArrowGenerationArgument(x, y - 1, arrowCount, Arrow.none)) }
        tryToGetArrowCount(x, y + 1)?.let { arrowCount -> arrowGenerationQueue.add(ArrowGenerationArgument(x, y + 1, arrowCount, Arrow.none)) }
        generateArrowMapInQueue()
    }

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
    private fun tryToGetArrowCount(x: Int, y: Int): Int? = try {
        fieldCountArray[y][x]
    } catch (e: ArrayIndexOutOfBoundsException) {
        null
    }

    /**
     * フィールド内の指定した位置の上下左右の隣にある矢印を取得する。位置が範囲外の場合はnullを返す。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param direction: Direction  矢印を取得するフィールドのxy位置からの方向
     * @return 上下左右の矢印またはnone
     */
    private fun tryToGetAdjacentArrow(x: Int, y: Int, direction: Direction): Arrow? = try {
        if (direction.isHorizontal()) {
            val x2 = if (direction == Direction.left) x - 1 else x
            getHorizontalArrow(x2, y)
        } else {
            val y2 = if (direction == Direction.top) y - 1 else y
            getVerticalArrow(x, y2)
        }
    } catch (e: ArrayIndexOutOfBoundsException) {
        null
    }

    /**
     *  左右の矢印マップから左右の矢印を取得する。
     * @param x : Int x位置
     * @param y: Int y位置
     */
    fun getHorizontalArrow(x: Int, y: Int): Arrow? = try {
        horizontalArrowArray[y][x]
    } catch (e: ArrayIndexOutOfBoundsException) {
        null
    }

    /**
     *  上下の矢印マップから上下の矢印を取得する。
     * @param x : Int x位置
     * @param y: Int y位置
     */
    fun getVerticalArrow(x: Int, y: Int): Arrow? = try {
        verticalArrowArray[y][x]
    } catch (e: ArrayIndexOutOfBoundsException) {
        null
    }

    /**
     *  フィールド内の指定した位置から左にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 左右どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    fun getLeftArrow(x: Int, y: Int): Arrow? = getHorizontalArrow(x - 1, y)

    /**
     *  フィールド内の指定した位置から右にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 左右どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    fun getRightArrow(x: Int, y: Int): Arrow? = getHorizontalArrow(x, y)

    /**
     *  フィールド内の指定した位置から上にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 上下どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    fun getTopArrow(x: Int, y: Int): Arrow? = getVerticalArrow(x, y - 1)

    /**
     *  フィールド内の指定した位置から下にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 上下どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    fun getBottomArrow(x: Int, y: Int): Arrow? = getVerticalArrow(x, y)

    /**
     * スタート地点からの移動距離を表すカウント変数を設定する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param count: Int カウント変数
     */
    fun setArrowCount(x: Int, y: Int, count: Int?): Boolean = try {
        fieldCountArray[y][x] = count
        true
    } catch (e: ArrayIndexOutOfBoundsException) {
        false
    }

    /**
     * フィールド内の指定した位置の上下左右の隣の矢印をセットする。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param direction: Direction  矢印を設定するフィールドのxy位置からの方向
     * @param arrow: Arrow 設定する矢印の種類
     */
    fun setArrowToAnySide(x: Int, y: Int, direction: Direction, arrow: Arrow) {
        when (direction) {
            Direction.left -> setArrowToLeftSide(x, y, arrow)
            Direction.right -> setArrowToRightSide(x, y, arrow)
            Direction.top -> setArrowToTopSide(x, y, arrow)
            Direction.bottom -> setArrowToBottomSide(x, y, arrow)
        }
    }

    /**
     *  フィールド内の指定した位置から左にある位置の矢印を設定する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param arrow: Arrow? 左右どちらかの矢印またはnone
     */
    fun setArrowToLeftSide(x: Int, y: Int, arrow: Arrow): Boolean = try {
        horizontalArrowArray[y][x - 1] = arrow
        true
    } catch (e: ArrayIndexOutOfBoundsException) {
        false
    }

    /**
     *  フィールド内の指定した位置から右にある位置の矢印を設定する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param arrow: Arrow? 左右どちらかの矢印またはnone
     */
    fun setArrowToRightSide(x: Int, y: Int, arrow: Arrow): Boolean = try {
        horizontalArrowArray[y][x] = arrow
        true
    } catch (e: ArrayIndexOutOfBoundsException) {
        false
    }

    /**
     *  フィールド内の指定した位置から上にある位置の矢印を設定する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param arrow: Arrow? 上下どちらかの矢印またはnone
     */
    fun setArrowToTopSide(x: Int, y: Int, arrow: Arrow): Boolean = try {
        verticalArrowArray[y - 1][x] = arrow
        true
    } catch (e: ArrayIndexOutOfBoundsException) {
        false
    }

    /**
     *  フィールド内の指定した位置から下にある位置の矢印を設定する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param arrow: Arrow? 上下どちらかの矢印またはnone
     */
    fun setArrowToBottomSide(x: Int, y: Int, arrow: Arrow) = try {
        verticalArrowArray[y][x] = arrow
        true
    } catch (e: ArrayIndexOutOfBoundsException) {
        false
    }

    /**
     * 指定した位置のカウントを削除して、上下左右の隣にある矢印もnoneにする。
     */
    private fun removeCountAndArrow(x: Int, y: Int) {
        fieldCountArray[y][x] = null
        setArrowToLeftSide(x, y, Arrow.none)
        setArrowToRightSide(x, y, Arrow.none)
        setArrowToTopSide(x, y, Arrow.none)
        setArrowToBottomSide(x, y, Arrow.none)
    }

    /**
     * 指定された位置に向かう矢印の本数を取得する。
     */
    private fun getReferredNum(x: Int, y: Int): Int {
        var num = 0
        if (getLeftArrow(x, y) == Arrow.right) num++
        if (getRightArrow(x, y) == Arrow.left) num++
        if (getTopArrow(x, y) == Arrow.bottom) num++
        if (getBottomArrow(x, y) == Arrow.top) num++
        return num
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

    private fun generateArrowMapInQueueNext(data: ArrowGenerationArgument): Boolean {
        val prev = data.prev
        val x = data.x
        val y = data.y

        //x,y位置が画面内にない場合、x,y位置にあるブロックが床でない場合は中断する。
        if (field.tryToGetFieldBlock(x, y)?.type != FieldBlockType.floor) {
            return false
        }

        //x,y位置のフィールドブロックの矢印カウントを取得する。
        val blockArrowCount = getArrowCount(x, y)

        if (prev != null) {
            //自身の現在のカウントより少ない矢印カウントのブロックにたどり着いた場合は中断する。
            if (data.arrowCount > blockArrowCount ?: Int.MAX_VALUE) return false
            //矢印を設定する。
            setArrowToAnySide(prev.x, prev.y, data.arrow.toDirection()!!, data.arrow)
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
        arrowGenerationQueue.add(ArrowGenerationArgument(x - 1, y, nextArrowCount, Arrow.left, prev = data))
        arrowGenerationQueue.add(ArrowGenerationArgument(x + 1, y, nextArrowCount, Arrow.right, prev = data))
        arrowGenerationQueue.add(ArrowGenerationArgument(x, y - 1, nextArrowCount, Arrow.top, prev = data))
        arrowGenerationQueue.add(ArrowGenerationArgument(x, y + 1, nextArrowCount, Arrow.bottom, prev = data))
    }

    /*
     * フィールド上の指定した位置から向かう矢印の鎖を削除する。
     * 指定した位置の上下左右の隣の矢印を調べ、指定した位置から外へ向かう矢印の場合は、削除を実行する。
     * @params x :Int フィールドのx位置
     * @params y :Int フィールドのy位置
     * @params isFirst  : Boolean 再起呼び出しでない場合はtrue、再起呼び出しの場合はfalse
     */
    fun removeArrowChain(x: Int, y: Int, isFirst: Boolean = false) {

        removeArrowChain_core(x, y)
        generateArrowMapInQueue()
    }

    private fun removeArrowChain_core(x: Int, y: Int) {
        val nearLeftArrow = tryToGetAdjacentArrow(x, y, Direction.left)
        val nearRightArrow = tryToGetAdjacentArrow(x, y, Direction.right)
        val nearTopArrow = tryToGetAdjacentArrow(x, y, Direction.top)
        val nearToBottomArrow = tryToGetAdjacentArrow(x, y, Direction.bottom)

        //現在位置の矢印カウントを削除して上下左右の隣の矢印もnoneにする。
        removeCountAndArrow(x, y)

        if (nearLeftArrow == Arrow.left) removeArrowChain_inner(x - 1, y)
        if (nearRightArrow == Arrow.right) removeArrowChain_inner(x + 1, y)
        if (nearTopArrow == Arrow.top) removeArrowChain_inner(x, y - 1)
        if (nearToBottomArrow == Arrow.bottom) removeArrowChain_inner(x, y + 1)
    }

    private fun removeArrowChain_inner(x: Int, y: Int) {
        if (getReferredNum(x, y) > 0) {
            arrowGenerationQueue.push(ArrowGenerationArgument(x, y, getArrowCount(x, y)!!, Arrow.none))
            return
        }
        removeArrowChain_core(x, y)
    }
}