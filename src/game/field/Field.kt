package game.field

import game.item.GameObject

/**
 * プレイヤーが行動するフィールドマップを表す。
 * マップ用の2次元配列とプレイヤーや敵キャラクタなどのオブジェクトのリストを内包する。
 * また、ある地点から別の地点への移動が壁によって遮られていないか否かを監視する矢印とカウントの情報(@seeFieldArrowLayer)を所有する。
 */
open class Field(val width: Int, val height: Int) {

    /**
     * フィールドマップを表現するためのフィールドブロックの2次元配列
     */
    private val fieldBlockArray = Array<Array<FieldBlock>>(height,init = {i -> Array(width, {i -> FieldBlock(FieldBlockType.floor)})})

    /**
     * フィールド上に存在する全てのゲームオブジェクトのリスト
     */
    private val gameObjects: MutableList<GameObject> = mutableListOf()

    /**
     * ある地点から別の地点への移動がさえぎられていないか否かを監視する矢印とカウントの情報
     */
    protected val arrowMap = FieldArrowMap(width, height, this)

    /**
     * 時間経過を表す値
     */
    private var timeCount = 0

    /**
     * 指定したx,y位置のフィールドブロックを取得する。
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    fun getFieldBlock(x: Int, y: Int): FieldBlock = fieldBlockArray[y][x]

    /**
     * 指定したx,y位置のフィールドブロックを設定する。
     *  @param x: Int フィールドの指定したx
     *  @param y: Int
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    fun setFieldBlock(x: Int, y: Int, fieldBlock: FieldBlock) {

        val oldBlockType = fieldBlockArray[y][x].type
        fieldBlockArray[y][x] = fieldBlock

        //床から壁に変更した場合は矢印の鎖の削除を行い再構築する。
        if (oldBlockType == FieldBlockType.floor && fieldBlock.type == FieldBlockType.wall) {
            arrowMap.removeArrowChain(x, y, isFirst = true)
            arrowMap.createArrowMapInQueue()
        //壁から床に変更した場合は矢印の鎖の再生成を行う。
        } else if (oldBlockType == FieldBlockType.wall && fieldBlock.type == FieldBlockType.floor) {
            arrowMap.regenerateBlockChain(x, y)
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

    /**
     * フィールドに移動する。
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    fun moveObject(gameObject: GameObject, x: Int, y: Int) {

        val prevX = gameObject.position.x
        val prevY = gameObject.position.y

        val result = fieldBlockArray[prevY][prevX].gameObjects.remove(gameObject)
        if (!result) throw Exception()


        fieldBlockArray[y][x].gameObjects.add(gameObject)

        gameObject.position.x = x
        gameObject.position.y = y

    }

    /**
     * フィールドからゲームオブジェクトを削除する。
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    fun removeObject(gameObject: GameObject) {
        gameObjects.remove(gameObject)
        gameObject.field = null

        val x = gameObject.position.x
        val y = gameObject.position.y
        fieldBlockArray[x][y].gameObjects.remove(gameObject)
    }


    /**
     * フィールド内で時間を1カウント経過させる。
     * 各オブジェクト(GameObject)のmoveInCountを実行する
     */
    fun passTime(): Boolean {
        for (obj in gameObjects) { obj.moveInCount() }
        timeCount++

        return true
    }

    /**
     * フィールドを文字列で描画する。
     */
    override fun toString(): String {
        var buf = StringBuffer()
        var buf2 = StringBuffer()

        for ( (y, fieldBlockRow) in fieldBlockArray.withIndex() ) {
            for ( (x, fieldBlock) in fieldBlockRow.withIndex() ) {

                val fieldCount :Int? = arrowMap.getArrowCount(x, y)
                buf.append(if(fieldBlock.gameObjects.size > 0 || fieldCount == null) fieldBlock.toString() else { String.format("%02d", fieldCount) })

                buf.append(arrowMap.tryToGetHorizonalArrow(x, y) ?: "")

                buf2.append(arrowMap.tryToGetVerticalArrow(x, y) ?: "")
                buf2.append("  ")
            }

            buf.append("\n${buf2}\n")
            buf2.setLength(0)
        }
        return buf.toString()
    }
}