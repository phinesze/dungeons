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
     * generateArrowMapInQueueを実行するのに必要なパラメータ
     * arrowGenerationQueueに格納される。
     */
    private class GenerateNextArrowParams(val x: Int, val y: Int, val arrowCount: Int, val arrow: Arrow, val prev: GenerateNextArrowParams? = null)

    /**
     * createArrowChainを実行するのに必要なパラメータを格納するキュー
     */
    private val generateNextArrowQueue = LinkedList<GenerateNextArrowParams>()

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
     * 指定された位置からの矢印マップを生成する。
     * 初めに指定された位置のカウントに0を設定して、
     */
    fun generateFieldArrowMap(x: Int, y: Int) {
        setArrowCount(x, y, count = 0)
        generateNextArrowQueue.push(GenerateNextArrowParams(x, y, arrowCount = 0, arrow = Arrow.none))
        executeGenerateArrowMapWithQueue()
    }

    /**
     * フィールド上の指定された位置が「壁」に変更された場合に矢印マップを再構築する。
     *
     * 指定した位置から向かう矢印の鎖を削除して、
     * 指定した位置の上下左右の隣の矢印を調べ、指定した位置から外へ向かう矢印の場合は、削除を実行する。
     * @params x :Int フィールドのx位置
     * @params y :Int フィールドのy位置
     * @params isFirst  : Boolean 再起呼び出しでない場合はtrue、再起呼び出しの場合はfalse
     */
    fun restructureArrowMap(x: Int, y: Int) {
        removeArrowChain(x, y)
        executeGenerateArrowMapWithQueue()
    }

    /**
     *  restructureArrowMapから呼び出される関数。
     *  当関数から再起呼び出しする場合は
     */
    private fun removeArrowChain(x: Int, y: Int) {
        val nearLeftArrow = getLeftSideArrow(x, y)
        val nearRightArrow = getRightSideArrow(x, y)
        val nearTopArrow = getTopSideArrow(x, y)
        val nearToBottomArrow = getBottomSideArrow(x, y)

        //現在位置の矢印カウントを削除して上下左右の隣の矢印もnoneにする。
        removeCountAndArrow(x, y)

        if (nearLeftArrow == Arrow.left) pushArrowGenerationQueueAndRemoveArrowChain(x - 1, y)
        if (nearRightArrow == Arrow.right) pushArrowGenerationQueueAndRemoveArrowChain(x + 1, y)
        if (nearTopArrow == Arrow.top) pushArrowGenerationQueueAndRemoveArrowChain(x, y - 1)
        if (nearToBottomArrow == Arrow.bottom) pushArrowGenerationQueueAndRemoveArrowChain(x, y + 1)
    }

    /**
     *
     */
    private fun pushArrowGenerationQueueAndRemoveArrowChain(x: Int, y: Int) {
        if (getReferredNum(x, y) > 0) {
            generateNextArrowQueue.push(GenerateNextArrowParams(x, y, getArrowCount(x, y)!!, Arrow.none))
            return
        }
        removeArrowChain(x, y)
    }

    fun regenerateBlockChain(x: Int, y: Int) {
        tryToGetArrowCount(x - 1, y)?.let { arrowCount -> generateNextArrowQueue.add(GenerateNextArrowParams(x - 1, y, arrowCount, Arrow.none)) }
        tryToGetArrowCount(x + 1, y)?.let { arrowCount -> generateNextArrowQueue.add(GenerateNextArrowParams(x + 1, y, arrowCount, Arrow.none)) }
        tryToGetArrowCount(x, y - 1)?.let { arrowCount -> generateNextArrowQueue.add(GenerateNextArrowParams(x, y - 1, arrowCount, Arrow.none)) }
        tryToGetArrowCount(x, y + 1)?.let { arrowCount -> generateNextArrowQueue.add(GenerateNextArrowParams(x, y + 1, arrowCount, Arrow.none)) }
        executeGenerateArrowMapWithQueue()
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
     * フィールド内の指定した位置の上下左右の隣にある矢印を取得する。位置が範囲外の場合はnullを返す。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param direction: Direction  矢印を取得するフィールドのxy位置からの方向
     * @return 上下左右の矢印またはnone
     */
    fun getAnySideArrow(x: Int, y: Int, direction: Direction): Arrow? = try {
        when (direction) {
            Direction.left -> getLeftSideArrow(x, y)
            Direction.right -> getRightSideArrow(x, y)
            Direction.top -> getTopSideArrow(x, y)
            Direction.bottom -> getBottomSideArrow(x, y)
        }
    } catch (e: ArrayIndexOutOfBoundsException) {
        null
    }

    /**
     *  フィールド内の指定した位置から左にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 左右どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    fun getLeftSideArrow(x: Int, y: Int): Arrow? = getHorizontalArrow(x - 1, y)

    /**
     *  フィールド内の指定した位置から右にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 左右どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    fun getRightSideArrow(x: Int, y: Int): Arrow? = getHorizontalArrow(x, y)

    /**
     *  フィールド内の指定した位置から上にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 上下どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    fun getTopSideArrow(x: Int, y: Int): Arrow? = getVerticalArrow(x, y - 1)

    /**
     *  フィールド内の指定した位置から下にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 上下どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    fun getBottomSideArrow(x: Int, y: Int): Arrow? = getVerticalArrow(x, y)

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
    fun setAnySideArrow(x: Int, y: Int, direction: Direction, arrow: Arrow) {
        when (direction) {
            Direction.left -> setLeftSideArrow(x, y, arrow)
            Direction.right -> setRightSideArrow(x, y, arrow)
            Direction.top -> setTopSideArrow(x, y, arrow)
            Direction.bottom -> setBottomSideArrow(x, y, arrow)
        }
    }

    /**
     *  フィールド内の指定した位置から左にある位置の矢印を設定する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param arrow: Arrow? 左右どちらかの矢印またはnone
     */
    fun setLeftSideArrow(x: Int, y: Int, arrow: Arrow): Boolean = try {
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
    fun setRightSideArrow(x: Int, y: Int, arrow: Arrow): Boolean = try {
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
    fun setTopSideArrow(x: Int, y: Int, arrow: Arrow): Boolean = try {
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
    fun setBottomSideArrow(x: Int, y: Int, arrow: Arrow) = try {
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
        setLeftSideArrow(x, y, Arrow.none)
        setRightSideArrow(x, y, Arrow.none)
        setTopSideArrow(x, y, Arrow.none)
        setBottomSideArrow(x, y, Arrow.none)
    }

    /**
     * 指定された位置に向かう矢印の本数を取得する。
     */
    private fun getReferredNum(x: Int, y: Int): Int {
        var num = 0
        if (getLeftSideArrow(x, y) == Arrow.right) num++
        if (getRightSideArrow(x, y) == Arrow.left) num++
        if (getTopSideArrow(x, y) == Arrow.bottom) num++
        if (getBottomSideArrow(x, y) == Arrow.top) num++
        return num
    }

    /**
     * キューに貯められたcreateNextArrowの実行待ちをすべて実行する。
     */
    fun executeGenerateArrowMapWithQueue() {
        while (generateNextArrowQueue.size > 0) {
            val params = generateNextArrowQueue.first
            generateNextArrowQueue.removeFirst()
            generateNextArrow(params)
        }
    }

    private fun generateNextArrow(param: GenerateNextArrowParams): Boolean {
        val prev = param.prev
        val x = param.x
        val y = param.y

        //x,y位置が画面内にない場合、x,y位置にあるブロックが床でない場合は中断する。
        if (field.tryToGetFieldBlock(x, y)?.type != FieldBlockType.floor) {
            return false
        }

        //x,y位置のフィールドブロックの矢印カウントを取得する。
        val blockArrowCount = getArrowCount(x, y)

        if (prev != null) {
            //自身の現在のカウントより少ない矢印カウントのブロックにたどり着いた場合は中断する。
            if (param.arrowCount > blockArrowCount ?: Int.MAX_VALUE) return false
            //矢印を設定する。
            setAnySideArrow(prev.x, prev.y, param.arrow.toDirection()!!, param.arrow)
            //カウントが同じになった場合、
            if (param.arrowCount == blockArrowCount) return false

        } else if (blockArrowCount == null) {
            return false
        }
        //ブロックに現在のカウントを代入する。
        setArrowCount(x, y, param.arrowCount)
        generateMazeArrowToQueue(param, x, y)
        return true
    }

    private fun generateMazeArrowToQueue(param: GenerateNextArrowParams, x: Int, y: Int) {
        val nextArrowCount = param.arrowCount + 1
        generateNextArrowQueue.add(GenerateNextArrowParams(x - 1, y, nextArrowCount, Arrow.left, prev = param))
        generateNextArrowQueue.add(GenerateNextArrowParams(x + 1, y, nextArrowCount, Arrow.right, prev = param))
        generateNextArrowQueue.add(GenerateNextArrowParams(x, y - 1, nextArrowCount, Arrow.top, prev = param))
        generateNextArrowQueue.add(GenerateNextArrowParams(x, y + 1, nextArrowCount, Arrow.bottom, prev = param))
    }
}