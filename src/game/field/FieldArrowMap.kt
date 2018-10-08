package game.field

import java.util.*

/**
 * 指定した位置から各フィールドブロックへの最短ルートと移動に必要な数を表すための
 * フィールドマップのブロック同士を上下につなぐ矢印のマップと各ブロックに1対1で対応する距離の値のマップ
 * 主にフィールドマップのスタート地点とゴール地点が移動可能かどうかを検証する。
 */
internal class FieldArrowMap(width: Int, height: Int, val field: Field) {

    /**
     * generateArrowMapInQueueを実行するのに必要なパラメータ
     * arrowGenerationQueueに格納される。
     */
    private class GenerateNextArrowParams(
            val x: Int,
            val y: Int,
            val distanceCount: Int,
            val arrow: Arrow,
            val prev: GenerateNextArrowParams? = null
    )

    /**
     * generateNextArrowを実行するのに必要なパラメータを格納するキュー
     */
    private val generateNextArrowQueue = LinkedList<GenerateNextArrowParams>()

    /**
     * フィールド上のブロックに1対1で対応するカウントの2次元配列
     */
    private val fieldCountArray = Array<Array<Int?>>(height) { it -> Array(width) { null } }

    /**
     * 左右の矢印の2次元配列
     */
    private val horizontalArrowArray = Array(height) { it -> Array(width - 1) { Arrow.None } }

    /**
     * 上下の矢印の2次元配列
     */
    private val verticalArrowArray = Array(height - 1) { it -> Array(width) { Arrow.None } }

    /**
     * 矢印マップがすでに生成されたか否か
     */
    var isGenerated :Boolean = false
        private set

    /**
     * 指定された位置からの矢印マップを生成する。
     * 初めに指定された位置のカウントに0を設定して、
     */
    fun generateFieldArrowMap(x: Int, y: Int) {
        setDistanceCount(x, y, count = 0)
        generateNextArrowQueue.push(GenerateNextArrowParams(x, y, distanceCount = 0, arrow = Arrow.None))
        executeGenerateArrowMapWithQueue()
        this.isGenerated = true
    }

    /**
     * フィールド上の指定された位置が「壁」に変更された場合に矢印マップを再構築する。
     *
     * 指定した位置から向かう矢印の鎖を削除して、
     * 指定した位置の上下左右の隣の矢印を調べ、指定した位置から外へ向かう矢印の場合は、削除を実行する。
     * @params x フィールドのx位置
     * @params y フィールドのy位置
     */
    fun restructureArrowMap(x: Int, y: Int) {
        removeArrowChain(x, y)
        executeGenerateArrowMapWithQueue()
    }

    /**
     * フィールド上の指定された位置が「床」に変更された場合に矢印マップを再生成する。
     * @params x フィールドのx位置
     * @params y フィールドのy位置
     */
    fun regenerateBlockChain(x: Int, y: Int) {
        tryToGetDistanceCount(x - 1, y)?.let { distanceCount -> generateNextArrowQueue.add(GenerateNextArrowParams(x - 1, y, distanceCount, Arrow.None)) }
        tryToGetDistanceCount(x + 1, y)?.let { distanceCount -> generateNextArrowQueue.add(GenerateNextArrowParams(x + 1, y, distanceCount, Arrow.None)) }
        tryToGetDistanceCount(x, y - 1)?.let { distanceCount -> generateNextArrowQueue.add(GenerateNextArrowParams(x, y - 1, distanceCount, Arrow.None)) }
        tryToGetDistanceCount(x, y + 1)?.let { distanceCount -> generateNextArrowQueue.add(GenerateNextArrowParams(x, y + 1, distanceCount, Arrow.None)) }
        executeGenerateArrowMapWithQueue()
    }

    /**
     * restructureArrowMapから呼び出される関数
     * フィールド上の指定した位置から矢印に沿うように距離カウントと矢印を削除していく。
     * 最初に指定した位置の距離カウントを削除し、その位置から上下左右の矢印が出ている場合は、
     * 矢印の先の位置も同様の処理を行うよう再起呼び出しする。
     * 再起呼び出しの際に指定した位置に矢印が向かっている場合はgenerateNextArrowQueueにパラメータを追加して
     * 矢印と距離カウントの再生成の準備をする。
     *
     * から向かう矢印の鎖を削除する。
     * @params x フィールドのx位置
     * @params y フィールドのy位置
     * @params isRecursive 再起呼び出しの場合か否か
     */
    private fun removeArrowChain(x: Int, y: Int, isRecursive: Boolean = false) {

        //再起呼び出しの場合に指定した位置に矢印が向かっている場合
        if (isRecursive && getReferredNum(x, y) > 0) {
            generateNextArrowQueue.push(GenerateNextArrowParams(x, y, getDistanceCount(x, y)!!, Arrow.None))
            return
        }

        //現在位置の距離カウントを削除
        this.setDistanceCount(x, y, null)

        //上下左右の矢印を取得した後にnoneに設定する。
        val nearLeftArrow = getLeftSideArrow(x, y)
        val nearRightArrow = getRightSideArrow(x, y)
        val nearTopArrow = getTopSideArrow(x, y)
        val nearToBottomArrow = getBottomSideArrow(x, y)
        setAllSideArrowNone(x, y)

        //矢印の向かう方向の位置で再起呼び出し
        if (nearLeftArrow == Arrow.Left) removeArrowChain(x - 1, y, isRecursive = true)
        if (nearRightArrow == Arrow.Right) removeArrowChain(x + 1, y, isRecursive = true)
        if (nearTopArrow == Arrow.Top) removeArrowChain(x, y - 1, isRecursive = true)
        if (nearToBottomArrow == Arrow.Bottom) removeArrowChain(x, y + 1, isRecursive = true)
    }

    /**
     * 指定した位置の上下左右の矢印をすべてnoneに設定する。
     */
    private fun setAllSideArrowNone(x: Int, y: Int) {
        setLeftSideArrow(x, y, Arrow.None)
        setRightSideArrow(x, y, Arrow.None)
        setTopSideArrow(x, y, Arrow.None)
        setBottomSideArrow(x, y, Arrow.None)
    }

    /**
     * スタート地点からの移動距離を表すカウント変数を取得する。
     *  @param x: Int フィールドのx位置
     *  @param y*Int フィールドのy位置
     *  @return Int? カウント変数
     */
    fun getDistanceCount(x: Int, y: Int): Int? = fieldCountArray[y][x]

    /**
     * スタート地点からの移動距離を表すカウント変数を取得する。x,y位置が範囲外の場合はnullを返す。
     *  @param x: Int フィールドのx位置
     *  @param y*Int フィールドのy位置
     *  @return Int? カウント変数
     */
    private fun tryToGetDistanceCount(x: Int, y: Int): Int? = try {
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
     *  フィールド内の指定した位置から左にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 左右どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    private fun getLeftSideArrow(x: Int, y: Int): Arrow? = getHorizontalArrow(x - 1, y)

    /**
     *  フィールド内の指定した位置から右にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 左右どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    private fun getRightSideArrow(x: Int, y: Int): Arrow? = getHorizontalArrow(x, y)

    /**
     *  フィールド内の指定した位置から上にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 上下どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    private fun getTopSideArrow(x: Int, y: Int): Arrow? = getVerticalArrow(x, y - 1)

    /**
     *  フィールド内の指定した位置から下にある位置の矢印を取得する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @return arrow? 上下どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    private fun getBottomSideArrow(x: Int, y: Int): Arrow? = getVerticalArrow(x, y)

    /**
     * スタート地点からの移動距離を表すカウント変数を設定する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param count: Int カウント変数
     */
    private fun setDistanceCount(x: Int, y: Int, count: Int?): Boolean = try {
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
    private fun setAnySideArrow(x: Int, y: Int, direction: Direction, arrow: Arrow) {
        when (direction) {
            Direction.Left -> setLeftSideArrow(x, y, arrow)
            Direction.Right -> setRightSideArrow(x, y, arrow)
            Direction.Top -> setTopSideArrow(x, y, arrow)
            Direction.Bottom -> setBottomSideArrow(x, y, arrow)
        }
    }

    /**
     *  フィールド内の指定した位置から左にある位置の矢印を設定する。
     * @param x : Int フィールドのx位置
     * @param y: Int フィールドのy位置
     * @param arrow: Arrow? 左右どちらかの矢印またはnone
     */
    private fun setLeftSideArrow(x: Int, y: Int, arrow: Arrow): Boolean = try {
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
    private fun setRightSideArrow(x: Int, y: Int, arrow: Arrow): Boolean = try {
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
    private fun setTopSideArrow(x: Int, y: Int, arrow: Arrow): Boolean = try {
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
    private fun setBottomSideArrow(x: Int, y: Int, arrow: Arrow) = try {
        verticalArrowArray[y][x] = arrow
        true
    } catch (e: ArrayIndexOutOfBoundsException) {
        false
    }

    /**
     * 指定された位置に向かう矢印の本数を取得する。
     */
    private fun getReferredNum(x: Int, y: Int): Int {
        var num = 0
        if (getLeftSideArrow(x, y) == Arrow.Right) num++
        if (getRightSideArrow(x, y) == Arrow.Left) num++
        if (getTopSideArrow(x, y) == Arrow.Bottom) num++
        if (getBottomSideArrow(x, y) == Arrow.Top) num++
        return num
    }

    /**
     * キューに貯められたcreateNextArrowの実行待ちをすべて実行する。
     */
    private fun executeGenerateArrowMapWithQueue() {
        while (generateNextArrowQueue.size > 0) {
            val params = generateNextArrowQueue.first
            generateNextArrowQueue.removeFirst()
            generateNextArrow(params)
        }
    }

    /**
     * executeGenerateArrowMapWithQueueから呼び出される。矢印と距離カウント変数を設定する。
     *
     */
    private fun generateNextArrow(param: GenerateNextArrowParams): Boolean {
        val prev = param.prev
        val x = param.x
        val y = param.y

        //x,y位置が画面内にない場合、x,y位置にあるブロックが床でない場合は中断する。
        val fieldBlock = field.tryToGetFieldBlock(x, y)
        if (fieldBlock == null || !fieldBlock.type.isFloor) return false

        //x,y位置のフィールドブロックの矢印カウントを取得する。
        val blockDistanceCount = getDistanceCount(x, y)

        if (prev != null) {
            //自身の現在のカウントより少ない矢印カウントのブロックにたどり着いた場合は中断する。
            if (param.distanceCount > blockDistanceCount ?: Int.MAX_VALUE) return false
            //矢印を設定する。
            setAnySideArrow(prev.x, prev.y, param.arrow.toDirection()!!, param.arrow)
            //カウントが同じになった場合、
            if (param.distanceCount == blockDistanceCount) return false

        } else if (blockDistanceCount == null) {
            return false
        }
        //ブロックに現在のカウントを代入する。
        setDistanceCount(x, y, param.distanceCount)
        generateMazeArrowToQueue(param, x, y)
        return true
    }

    private fun generateMazeArrowToQueue(param: GenerateNextArrowParams, x: Int, y: Int) {
        val nextDistanceCount = param.distanceCount + 1
        generateNextArrowQueue.add(GenerateNextArrowParams(x - 1, y, nextDistanceCount, Arrow.Left, prev = param))
        generateNextArrowQueue.add(GenerateNextArrowParams(x + 1, y, nextDistanceCount, Arrow.Right, prev = param))
        generateNextArrowQueue.add(GenerateNextArrowParams(x, y - 1, nextDistanceCount, Arrow.Top, prev = param))
        generateNextArrowQueue.add(GenerateNextArrowParams(x, y + 1, nextDistanceCount, Arrow.Bottom, prev = param))
    }
}