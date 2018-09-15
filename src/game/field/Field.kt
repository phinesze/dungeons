package game.field

import game.item.GameObject

/**
 * プレイヤーが行動するフィールドマップを表す。
 * マップ用の2次元配列とプレイヤーや敵キャラクタなどのオブジェクトのリストを内包する。
 * また、ある地点から別の地点への移動が壁によって遮られていないか否かを監視する矢印とカウントの情報(@seeFieldArrowLayer)を所有する。
 */
open  class Field(val width: Int, val height: Int, val floor :Int = 0) {

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
    internal val arrowMap = FieldArrowMap(width, height, this)

    /**
     * 時間経過を表す値
     */
    private var timeCount = 0

    /**
     *  フィールド間でのマップ移動を検知した事をゲームボードに通知する。
     */
    var mapMoveId: Int?= null

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

        addObjectInFieldBlockArray(x, y, gameObject)

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
        try { removeObjectFromFieldBlockArray(prevX, prevY, gameObject) } catch(e :Exception) {}
        addObjectInFieldBlockArray(x, y, gameObject)
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

        removeObjectFromFieldBlockArray(x, y, gameObject)
    }

    private fun addObjectInFieldBlockArray(x: Int, y: Int, gameObject: GameObject) {
        val fieldBlock = fieldBlockArray[y][x]
        fieldBlock.gameObjects.add(gameObject)
        gameObject.fieldBlock = fieldBlock
    }

    private fun removeObjectFromFieldBlockArray(x: Int, y: Int, gameObject: GameObject) {
        val fieldBlock = fieldBlockArray[y][x]
        fieldBlock.gameObjects.remove(gameObject)
        gameObject.fieldBlock = null
    }

    /**
     * フィールド内で時間を1カウント経過させる。
     * 各オブジェクト(GameObject)のmoveInCountを実行して、オブジェクト同士の重なり検知関数を呼び出す。
     */
    fun count(): Boolean {
        for (obj in gameObjects) { obj.onCount() }
        collisionDetect()
        timeCount++

        return true
    }

    /**
     *  フィールド内のゲームオブジェクト(GameObject)同士が重なっていないかどうかをチェックする。
     *   初めにフィールド内のすべてのゲームオブジェクトをfor in文でループさせ、targetObjectAとする。そのループ中でそのゲームオブジェクトが属する
     *   フィールドブロック(FieldBlock)各オブジェクトで再度for inを掛け、targetObjectBとする。
     *   targetObjectと
     */
    private fun collisionDetect() {
        for (targetObjectA: GameObject in this.gameObjects) {
            for(targetObjectB: GameObject in targetObjectA.fieldBlock!!.gameObjects) {
                if (targetObjectA === targetObjectB) break
                    targetObjectA.collisionDetected(targetObjectB)
                    targetObjectB.collisionDetected(targetObjectA)
            }
        }
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

                buf.append(arrowMap.getHorizontalArrow(x, y) ?: "")

                buf2.append(arrowMap.getVerticalArrow(x, y) ?: "")
                buf2.append("  ")
            }

            buf.append("\n${buf2}\n")
            buf2.setLength(0)
        }
        return buf.toString()
    }
}