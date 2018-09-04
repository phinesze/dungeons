package game.field

import game.item.GameObject
import java.util.*

/**
 * プレイヤーが行動するフィールドマップを表す。
 * マップ用の2次元配列とプレイヤーや敵キャラクタなどのオブジェクトのリストを内包する。
 */
open class Field(val width: Int, val height: Int) {

//    val gameObjects: Array<GameObject> = arrayOf()

    /**
     * createArrowChainを実行するのに必要なパラメータのキュー
     */
    private class createArrowChainQueue(val x: Int, val y: Int, val arrowCount: Int, val arrow: Arrow, val prev: createArrowChainQueue? = null)

    /**
     * フィールドマップを表現するためのフィールドブロックの2次元配列
     */
    private val fieldBlockArray = Array<Array<FieldBlock>>(height,init = {i -> Array(width, {i -> FieldBlock(FieldBlockType.floor)})})

    /**
     * フィールド上に存在する全てのゲームオブジェクトのリスト
     */
    private val gameObjects: MutableList<GameObject> = mutableListOf()

    /**
     * 矢印とカウントの情報
     */
    protected val arrowLayer = FieldArrowLayer(width, height)

    /**
     * 時間経過を表す値
     */
    private var timeCount = 0

    /**
     * デバッグ用カウント
     */
    private var debugCount = 0

    /**
     *
     */
    private val generateMazeQueue = LinkedList<createArrowChainQueue>()

    /**
     * 指定したx,y位置のフィールドブロックを取得する。
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    fun getFieldBlock(x: Int, y: Int): FieldBlock = fieldBlockArray[y][x]

    /**
     * 指定したx,y位置のフィールドブロックを設定する。
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    fun setFieldBlock(x: Int, y: Int, fieldBlock: FieldBlock) {

        val oldBlockType = fieldBlockArray[y][x].type
        fieldBlockArray[y][x] = fieldBlock

        //床から壁に変更した場合は矢印の鎖の削除を行い再構築する。
        if (oldBlockType == FieldBlockType.floor && fieldBlock.type == FieldBlockType.wall) {

            removeArrowChain(x, y, isFirst = true)
            createArrowChainInQueue()

        //壁から床に変更した場合は矢印の鎖の再生成を行う。
        } else if (oldBlockType == FieldBlockType.wall && fieldBlock.type == FieldBlockType.floor) {

            arrowLayer.tryToGetArrowCount(x - 1, y)?.let { arrowCount -> generateMazeQueue.add(createArrowChainQueue(x - 1, y, arrowCount, Arrow.none)) }
            arrowLayer.tryToGetArrowCount(x + 1, y)?.let { arrowCount -> generateMazeQueue.add(createArrowChainQueue(x + 1, y, arrowCount, Arrow.none)) }
            arrowLayer.tryToGetArrowCount(x, y - 1)?.let { arrowCount -> generateMazeQueue.add(createArrowChainQueue(x, y - 1, arrowCount, Arrow.none)) }
            arrowLayer.tryToGetArrowCount(x, y + 1)?.let { arrowCount -> generateMazeQueue.add(createArrowChainQueue(x, y + 1, arrowCount, Arrow.none)) }
            createArrowChainInQueue()
        }
    }

    /**
     * 指定したx,y位置のフィールドブロックを取得する。x,y位置が範囲外の場合はnullを返す。
     */
    fun tryToGetFieldBlock(x: Int, y: Int): FieldBlock? = try { getFieldBlock(x, y) } catch (e: ArrayIndexOutOfBoundsException) { null }

    /**
     * 指定したx,y位置のフィールドブロックを設定する。x,y位置が範囲外の場合はnullを返す。
     */
    fun tryToSetFieldBlock(x: Int, y: Int, fieldBlock: FieldBlock) { try { setFieldBlock(x, y, fieldBlock) } catch (e: ArrayIndexOutOfBoundsException) { null } }

    /**
     * フィールドの指定したx, y位置にゲームオブジェクトを追加する。
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    fun addObject(x: Int, y: Int, gameObject: GameObject) {
        gameObjects.add(gameObject)
        gameObject.field = this

        fieldBlockArray[y][x].gameObjects.add(gameObject)

        gameObject.position.x = x
        gameObject.position.y = y
    }

    /* 指定された位置からの矢印の鎖を生成する。*/
    protected fun createArrowChain(x: Int, y: Int) {

        arrowLayer.setArrowCount(x, y, count = 0)
        generateMazeQueue.push(createArrowChainQueue(x, y, 0, Arrow.none))

        createArrowChainInQueue()
    }

    /**
     * キューに貯められたcreateNextArrowの実行待ちをすべて実行する。
     */
    private fun createArrowChainInQueue() {

        while (generateMazeQueue.size > 0) {

            val params = generateMazeQueue.getFirst()
            generateMazeQueue.removeFirst()
            createArrowChainInQueueNext(params)
        }
    }

    private fun createArrowChainInQueueNext(data : createArrowChainQueue): Boolean {
        val prev = data.prev
        val x = data.x
        val y = data.y

        //x,y位置が画面内にない場合、x,y位置にあるブロックが床でない場合は中断する。
        if (this.tryToGetFieldBlock(x, y)?.type != FieldBlockType.floor) { return false }

        //x,y位置のフィールドブロックの矢印カウントを取得する。
        val blockArrowCount = arrowLayer.getArrowCount(x, y)

        if (prev != null) {
            //自身の現在のカウントより少ない矢印カウントのブロックにたどり着いた場合は中断する。
            if (data.arrowCount > blockArrowCount ?: Int.MAX_VALUE) return false

            //矢印を設定する。
            arrowLayer.setArrow(prev.x, prev.y, data.arrow.toDirection()!!, data.arrow, ifNone = false)

            //カウントが同じになった場合、
            if (data.arrowCount == blockArrowCount) return false

        } else if (blockArrowCount == null) {
            return false
        }

        //ブロックに現在のカウントを代入する。
        arrowLayer.setArrowCount(x, y, data.arrowCount)

        generateMazeArrowToQueue(data, x, y)

        println("createArrowChainInQueueNext: debugcount = ${debugCount}"); println(this); debugCount++;

        return true
    }

    private fun generateMazeArrowToQueue(data: createArrowChainQueue, x: Int, y: Int) {
        val nextArrowCount = data.arrowCount + 1
        generateMazeQueue.add(createArrowChainQueue(x - 1, y,     nextArrowCount, Arrow.left,   prev = data))
        generateMazeQueue.add(createArrowChainQueue(x + 1, y,     nextArrowCount, Arrow.right,  prev = data))
        generateMazeQueue.add(createArrowChainQueue(x,     y - 1, nextArrowCount, Arrow.top,    prev = data))
        generateMazeQueue.add(createArrowChainQueue(x,     y + 1, nextArrowCount, Arrow.bottom, prev = data))
    }

    /*
     * 矢印の鎖を削除する。
     * @params x フィールドのx位置
     * @params y フィールドのy位置
     * @params type フィールドブロックのタイプ
     */
    private fun removeArrowChain(x: Int, y: Int, isFirst: Boolean = false) {

        if (!isFirst && arrowLayer.getReferedNum(x, y) > 0) {
            generateMazeQueue.push(createArrowChainQueue(x, y, arrowLayer.getArrowCount(x, y)!!, Arrow.none))

            println("removeArrowChainReturn: debugcount = ${debugCount}"); println(this);
            return
        }

        val nearLeftArrow = arrowLayer.tryToGetArrow(x, y, Direction.left)
        val nearRightArrow = arrowLayer.tryToGetArrow(x, y, Direction.right)
        val nearTopArrow = arrowLayer.tryToGetArrow(x, y, Direction.top)
        val nearToBottomArrow = arrowLayer.tryToGetArrow(x, y, Direction.bottom)

        //現在位置の矢印カウントを削除して上下左右の隣の矢印もnoneにする。
        arrowLayer.removeCountAndArrow(x, y)

        println("removeArrowChain: debugcount = ${debugCount}"); println(this); debugCount++;

        if (nearLeftArrow == Arrow.left) removeArrowChain(x - 1, y)

        if (nearRightArrow == Arrow.right) removeArrowChain(x + 1, y)

        if (nearTopArrow == Arrow.top) removeArrowChain(x, y - 1)

        if (nearToBottomArrow == Arrow.bottom) removeArrowChain(x, y + 1)
    }

    /**
     * フィールド内で時間を1カウント経過させる。
     * 各オブジェクト(GameObject)のmoveInCountを実行する
     */
    fun passTime() {
        for (obj in gameObjects) { obj.moveInCount() }
        timeCount++
    }

    /**
     * フィールドを文字列で描画する。
     */
    override fun toString(): String {
        var buf = StringBuffer()
        var buf2 = StringBuffer()

        for ( (y, fieldBlockRow) in fieldBlockArray.withIndex() ) {
            for ( (x, fieldBlock) in fieldBlockRow.withIndex() ) {

                val fieldCount :Int? = arrowLayer.getArrowCount(x, y)
                buf.append(if(fieldBlock.gameObjects.size > 0 || fieldCount == null) fieldBlock.toString() else { String.format("%02d", fieldCount) })

                buf.append(arrowLayer.tryToGetHorizonalArrow(x, y) ?: "")

                buf2.append(arrowLayer.tryToGetVerticalArrow(x, y) ?: "")
                buf2.append("  ")
            }

            buf.append("\n${buf2}\n")
            buf2.setLength(0)
        }
        return buf.toString()
    }
}