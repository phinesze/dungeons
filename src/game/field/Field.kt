package game.field

import game.item.GameObject
import game.item.Player

/**
 * プレイヤーが行動するフィールドマップを表す。
 * マップ用の2次元配列とプレイヤーや敵キャラクタなどのオブジェクトのリストを内包する。
 * また、ある地点から別の地点への移動が壁によって遮られていないか否かを監視する矢印とカウントの情報(@seeFieldArrowLayer)を所有する。
 * @property width フィールドの幅を表す数値
 * @property height フィールドの高さを表す数値
 * @property  floor 現在の階層を表す数値
 */
open  class Field(val width: Int, val height: Int, val floor :Int = 0) {

    /**
     * フィールドマップを表現するためのフィールドブロックの2次元配列
     */
    private val fieldBlockArray = Array<Array<FieldBlock>>(height,init = {i -> Array(width) { FieldBlock(FieldBlockType.floor)} })

    /**
     * フィールド上に存在する全てのゲームオブジェクトのリスト
     */
    private val gameObjects: MutableList<GameObject> = mutableListOf()

    /**
     * ゲームオブジェクトの内のプレイヤーオブジェクトのリスト
     */
    private val playerObjects:  MutableList<Player> = mutableListOf()

    /**
     *  削除予定のゲームオブジェクトのリスト
     */
    private val trashObjects:  MutableList<GameObject> = mutableListOf()

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
     *  toString時にデバッグ用に矢印マップを出力するか否かの値
     */
    private var printArrowMap: Boolean = false

    /**
     *  フィールド内のプレイヤーオブジェクトを取得する。
     *  @param index インデックス
     *  @return 取得したプレイヤーオブジェクト
     */
    fun getPlayer(index: Int = 0): Player {
        return playerObjects[index]
    }

    /**
     * 指定したx,y位置のフィールドブロックを取得する。
     * @param x フィールド内のx位置
     * ＠param y フィールド内のy位置
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    fun getFieldBlock(x: Int, y: Int): FieldBlock = fieldBlockArray[y][x]

    /**
     * 指定したx,y位置のフィールドブロックを設定する。
     *  @param x フィールド内のx位置
     *  @param y フィールド内のy位置
     *  @param fieldBlock 設定するフィールドブロック
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    fun setFieldBlock(x: Int, y: Int, fieldBlock: FieldBlock) {

        val oldBlockType = fieldBlockArray[y][x].type
        fieldBlockArray[y][x] = fieldBlock

        //床から壁に変更した場合は矢印の鎖の削除を行い再構築する。
        if (oldBlockType == FieldBlockType.floor && fieldBlock.type == FieldBlockType.wall) {
            arrowMap.restructureArrowMap(x, y)
        //壁から床に変更した場合は矢印の鎖の再生成を行う。
        } else if (oldBlockType == FieldBlockType.wall && fieldBlock.type == FieldBlockType.floor) {
            arrowMap.regenerateBlockChain(x, y)
        }
    }

    /**
     * 指定したx,y位置のフィールドブロックを取得する。x,y位置が範囲外の場合はnullを返す。
     *  @param x フィールド内のx位置
     *  @param y フィールド内のy位置
     *  @return  取得したフィールドブロック
     */
    fun tryToGetFieldBlock(x: Int, y: Int): FieldBlock? = try { getFieldBlock(x, y) } catch (e: ArrayIndexOutOfBoundsException) { null }

    /**
     * 指定したx,y位置のフィールドブロックを設定する。x,y位置が範囲外の場合はnullを返す。
     *  @param x フィールド内のx位置
     *  @param y フィールド内のy位置
     *  @param fieldBlock 設定するフィールドブロック
     */
    fun tryToSetFieldBlock(x: Int, y: Int, fieldBlock: FieldBlock) { try { setFieldBlock(x, y, fieldBlock) } catch (e: ArrayIndexOutOfBoundsException) { null } }

    /**
     * フィールド内のx, y位置にゲームオブジェクトを追加する。
     * 追加されたゲームオブジェクトのx, y位置は指定した位置に変更される。
     * @param x フィールド内のx位置
     * @param y フィールド内のy位置
     * @param gameObject 追加するゲームオブジェクト
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    fun addObject(x: Int, y: Int, gameObject: GameObject) {

        this.gameObjects.add(gameObject)
        if (gameObject is Player) this.playerObjects.add(gameObject)
        //
        addObjectInFieldBlock(x, y, gameObject)
        //ゲームオブジェクトの位置を指定された位置に変更
        gameObject.position.x = x
        gameObject.position.y = y
        gameObject.field = this
    }

    /**
     * フィールド内のx, y位置に指定したゲームオブジェクトに移動する。
     * @param x フィールド内のx位置
     * @param y フィールド内のy位置
     * @param gameObject  移動するゲームオブジェクト
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    fun moveObject(x: Int, y: Int, gameObject: GameObject) {
        //移動
        val prevX = gameObject.position.x
        val prevY = gameObject.position.y
        try { removeObjectFromFieldBlock(prevX, prevY, gameObject) } catch(e :Exception) {}
        addObjectInFieldBlock(x, y, gameObject)
        gameObject.position.x = x
        gameObject.position.y = y
    }

    /**
     * フィールド内のx, y位置に移動を妨げるオブジェクトがある場合にその中で一番上にあるオブジェクトを返す。
     * @param x フィールド内のx位置
     * @param y フィールド内のy位置
     * @return 指定された位置の一番上にあるゲームオブジェクト。存在しない場合はnullを返す。
     */
    fun getNotThroughable(x: Int, y: Int): GameObject? {
        val fieldBlock = this.getFieldBlock(x, y)
        for(obj in fieldBlock.gameObjects) {
            if (!obj.isThroughable) return obj
        }
        return null
    }

    /**
     *  削除予定のオブジェクトにゲームオブジェクトを追加する。
     *  対象となったゲームオブジェクトは削除予定のゲームオブジェクトのリストに追加され、1カウント経過後の処理でリスト内のすべてのオブジェクトに
     *  removeObjectを実行して削除予定リストを空にする。
     *  @param gameObject  削除予定のゲームオブジェクト
     */
    fun trashObject(gameObject: GameObject) {
        trashObjects.add(gameObject)
    }

    /**
     * フィールドからゲームオブジェクトを削除する。
     *  直接呼び出さずに1カウント経過後に削除予定のオブジェクトリストに入っている場合に実行される。
     * @params gameObject 削除するゲームオブジェクト
     * @throws ArrayIndexOutOfBoundsException x,y位置が範囲外の場合に返す。
     */
    private fun removeObject(gameObject: GameObject) {
        gameObjects.remove(gameObject)
        if (gameObject is Player) playerObjects.remove(gameObject)

        val x = gameObject.position.x
        val y = gameObject.position.y

        removeObjectFromFieldBlock(x, y, gameObject)
    }

    /**
     *  ゲームオブジェクトをフィールド内ブロックのオブジェクトリストに追加する。
     *  @param x フィールド内のx位置
     *  @param y フィールド内のy位置
     *  @param gameObject 追加するゲームオブジェクト
     */
    private fun addObjectInFieldBlock(x: Int, y: Int, gameObject: GameObject) {
        val fieldBlock = fieldBlockArray[y][x]
        fieldBlock.gameObjects.add(gameObject)
        gameObject.fieldBlock = fieldBlock
    }

    /**
     *  ゲームオブジェクトをフィールド内ブロックのオブジェクトリストから削除する。
     *  @param x フィールド内のx位置
     *  @param y フィールド内のy位置
     *  @param gameObject  削除するゲームオブジェクト
     */
    private fun removeObjectFromFieldBlock(x: Int, y: Int, gameObject: GameObject) {
        val fieldBlock = fieldBlockArray[y][x]
        fieldBlock.gameObjects.remove(gameObject)
        gameObject.fieldBlock = null
    }

    /**
     * フィールド内で時間を1カウント経過させる。
     * 各オブジェクト(GameObject)のonCountを実行して、オブジェクト同士の重なり検知関数を呼び出す。
     */
    fun count(): Boolean {

        //各オブジェクトのonCountを実行
        for (obj in gameObjects) obj.onCount()
        //削除予定のオブジェクトリストにあるオブジェクトを実際に削除F
        for(trashObject in trashObjects) this.removeObject(trashObject)
        trashObjects.clear()
        //重なり検知
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

                var fieldCount: Int? = null
                if (this.printArrowMap)
                    fieldCount = arrowMap.getArrowCount(x, y)

                val str = if (fieldBlock.gameObjects.size > 0 || fieldCount == null) fieldBlock.toString() else String.format("%02d", fieldCount)
                buf.append(str)

                if (this.printArrowMap) {
                    buf.append(arrowMap.getHorizontalArrow(x, y) ?: "")
                    buf2.append(arrowMap.getVerticalArrow(x, y) ?: "")
                    buf2.append("  ")
                }
            }

            buf.append(if (this.printArrowMap) {"\n${buf2}\n"} else {"\n"})
            buf2.setLength(0)
        }
        return buf.toString()
    }
}