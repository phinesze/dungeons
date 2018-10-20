package game.field

import java.util.*

/**
 * フィールドマップのスタート地点と各ブロックの地点まで移動可能かどうか、移動可能な場合はスタートから各ブロックまでの
 * 最短距離を測るための距離カウントと矢印のマップ表す。
 * フィールドマップの各ブロックに対応する距離カウントのマップとブロック同士を繋ぐように上下と左右に隣接する矢印のマップで表される。
 * generateFieldArrowMapを実行することによって距離カウントと矢印が生成される。
 *
 * @param width フィールドの幅を表す数値
 * @param height フィールドの高さを表す数値
 * @property field 対象となるフィールド
 */
internal class FieldArrowMap(width: Int, height: Int, val field: Field) {

    /**
     * generateArrowMapInQueueを実行するのに必要なパラメータ
     * arrowGenerationQueueに格納される。
     * @property x フィールド上のx位置
     * @property y フィールド上のx位置
     * @property distanceCount 距離カウント
     * @property arrow 矢印
     * @property prev
     */
    private class GenerateNextArrowParams(val x: Int, val y: Int, val distanceCount: Int, val arrow: Arrow, val prev: GenerateNextArrowParams? = null)

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
     * 指定された位置をスタート地点として距離カウントと矢印のマップを生成する。
     * 初めに指定されたスタート地点の距離カウントに0を設定して、上下左右の隣接するブロックが通過可能な場合に
     * その位置に向かう矢印を設定して隣接したブロックの距離カウントに1を設定する。
     *
     * 隣接したブロックでも上下左右のブロックが通過可能かつカウント未設定の場合に同様に矢印を設定して距離カウントに(1つ前のカウント+1)を設定する。
     * 同様の同じ処理を繰り返していき、通過可能な場所がなくなった場合に生成を終了する。
     *
     * @param x スタート地点となるフィールド上のx位置
     * @param y スタート地点となるフィールド上のy位置
     */
    fun generateFieldArrowMap(x: Int, y: Int) {
        setDistanceCount(x, y, 0)
        prepareToGenerateNextArrow(x, y, distanceCount = 0, arrow = Arrow.None, prev = null)
        executeGenerateAllArrow()
        this.isGenerated = true
    }

    /**
     * generateNextArrow実行のためのキューをためて実行待ちにする。
     */
    private fun prepareToGenerateNextArrow(x: Int, y: Int, distanceCount: Int, arrow: Arrow, prev: GenerateNextArrowParams?) {
        generateNextArrowQueue.addLast(GenerateNextArrowParams(x, y, distanceCount, arrow, prev))
    }

    /**
     * キューに貯められたgenerateNextArrowの実行待ちをすべて実行する。
     */
    private fun executeGenerateAllArrow() {
        while (generateNextArrowQueue.size > 0) {
            generateNextArrow(generateNextArrowQueue.pollFirst())
        }
    }

    /**
     * executeGenerateAllArrowから呼び出される。矢印と距離カウント変数を設定する。
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
        prepareToGenerateNextArrow(x - 1, y, nextDistanceCount, Arrow.Left, prev = param)
        prepareToGenerateNextArrow(x + 1, y, nextDistanceCount, Arrow.Right, prev = param)
        prepareToGenerateNextArrow(x, y - 1, nextDistanceCount, Arrow.Top, prev = param)
        prepareToGenerateNextArrow(x, y + 1, nextDistanceCount, Arrow.Bottom, prev = param)
    }

    /**
     * フィールド上の指定された位置が「壁」に変更された場合に矢印マップを再構築する。
     * @params x フィールドのx位置
     * @params y フィールドのy位置
     */
    fun restructureArrowMap(x: Int, y: Int) {
        removeArrowChain(x, y)
        executeGenerateAllArrow()
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
        executeGenerateAllArrow()
    }

    /**
     * restructureArrowMapから呼び出される関数
     * フィールド上の指定した位置から矢印の向かう方向へと距離カウントと矢印を削除していく。
     * 最初に指定した位置の距離カウントを削除し、その位置から上下左右の矢印が伸びている場合は、矢印の先の位置も同様の処理を行うよう再起呼び出しする。
     * 再起呼び出しの際に指定した位置に矢印が向かっている場合はgenerateNextArrowQueueにパラメータを追加して矢印と距離カウントの再生成の準備をする。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @param isRecursive 再起呼び出しの場合か否か
     */
    private fun removeArrowChain(x: Int, y: Int, isRecursive: Boolean = false) {

        //再起呼び出しの場合に指定した位置に矢印が向かっている場合はgenerateNextArrowQueueに追加する。
        if (isRecursive && getReferredNum(x, y) > 0) {
            prepareToGenerateNextArrow(x, y, distanceCount = getDistanceCount(x, y)!!, arrow = Arrow.None, prev = null)
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
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     */
    private fun setAllSideArrowNone(x: Int, y: Int) {
        setLeftSideArrow(x, y, Arrow.None)
        setRightSideArrow(x, y, Arrow.None)
        setTopSideArrow(x, y, Arrow.None)
        setBottomSideArrow(x, y, Arrow.None)
    }

    /**
     * スタート地点からの移動距離を表すカウント変数を取得する。
     *  @param x フィールドのx位置
     *  @param y フィールドのy位置
     *  @return カウント変数
     */
    fun getDistanceCount(x: Int, y: Int): Int? = fieldCountArray[y][x]

    /**
     * スタート地点からの移動距離を表すカウント変数を取得する。x,y位置が範囲外の場合はnullを返す。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @return カウント変数
     */
    private fun tryToGetDistanceCount(x: Int, y: Int): Int? = try {
        fieldCountArray[y][x]
    } catch (e: ArrayIndexOutOfBoundsException) {
        null
    }

    /**
     * 左右の矢印マップから左右の矢印を取得する。
     * @param x x位置
     * @param y y位置
     */
    fun getHorizontalArrow(x: Int, y: Int): Arrow? = try {
        horizontalArrowArray[y][x]
    } catch (e: ArrayIndexOutOfBoundsException) {
        null
    }

    /**
     * 上下の矢印マップから上下の矢印を取得する。
     * @param x x位置
     * @param y y位置
     * @return
     */
    fun getVerticalArrow(x: Int, y: Int): Arrow? = try {
        verticalArrowArray[y][x]
    } catch (e: ArrayIndexOutOfBoundsException) {
        null
    }

    /**
     * フィールド内の指定した位置から左にある位置の矢印を取得する。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @return 左右どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    private fun getLeftSideArrow(x: Int, y: Int): Arrow? = getHorizontalArrow(x - 1, y)

    /**
     * フィールド内の指定した位置から右にある位置の矢印を取得する。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @return 左右どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    private fun getRightSideArrow(x: Int, y: Int): Arrow? = getHorizontalArrow(x, y)

    /**
     * フィールド内の指定した位置から上にある位置の矢印を取得する。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @return 上下どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    private fun getTopSideArrow(x: Int, y: Int): Arrow? = getVerticalArrow(x, y - 1)

    /**
     * フィールド内の指定した位置から下にある位置の矢印を取得する。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @return 上下どちらかの矢印またはnone、範囲外の場合はnullを返す。
     */
    private fun getBottomSideArrow(x: Int, y: Int): Arrow? = getVerticalArrow(x, y)

    /**
     * スタート地点からの移動距離を表すカウント変数を設定する。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @param distanceCount カウント変数
     */
    private fun setDistanceCount(x: Int, y: Int, distanceCount: Int?): Boolean = try {
        fieldCountArray[y][x] = distanceCount
        true
    } catch (e: ArrayIndexOutOfBoundsException) {
        false
    }

    /**
     * フィールド内の指定した位置の上下左右の隣の矢印をセットする。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @param direction 矢印を設定するフィールドのxy位置からの方向
     * @param arrow 設定する矢印の種類
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
     * フィールド内の指定した位置から左にある位置の矢印を設定する。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @param arrow 左右どちらかの矢印またはnone
     */
    private fun setLeftSideArrow(x: Int, y: Int, arrow: Arrow): Boolean = try {
        horizontalArrowArray[y][x - 1] = arrow
        true
    } catch (e: ArrayIndexOutOfBoundsException) {
        false
    }

    /**
     * フィールド内の指定した位置から右にある位置の矢印を設定する。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @param arrow 左右どちらかの矢印またはnone
     */
    private fun setRightSideArrow(x: Int, y: Int, arrow: Arrow): Boolean = try {
        horizontalArrowArray[y][x] = arrow
        true
    } catch (e: ArrayIndexOutOfBoundsException) {
        false
    }

    /**
     * フィールド内の指定した位置から上にある位置の矢印を設定する。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @param arrow 上下どちらかの矢印またはnone
     */
    private fun setTopSideArrow(x: Int, y: Int, arrow: Arrow): Boolean = try {
        verticalArrowArray[y - 1][x] = arrow
        true
    } catch (e: ArrayIndexOutOfBoundsException) {
        false
    }

    /**
     * フィールド内の指定した位置から下にある位置の矢印を設定する。
     * @param x フィールドのx位置
     * @param y フィールドのy位置
     * @param arrow 上下どちらかの矢印またはnone
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
}