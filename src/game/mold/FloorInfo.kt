package game.mold

import game.datalist.floorList
import java.util.*

/**
 * フロアごとの情報を表す
 * @property enemyIdsParam フロアに出現する敵の種類のIDの配列、enemyListのキーの値を表す
 * @property enemyNumParam フロアに出現する敵の数
 * @property itemIdsParam フロア内に落ちているのアイテムの種類のIDの配列、itenListのキーの値を表す。
 * @property itemNumParam フロア内の落ちているアイテムの数
 * @property fieldWidthParam フロアの幅を表す数値
 * @property fieldHeightParam フロアの高さを表す数値
 * @property inherits
 */
class FloorInfo(
        var enemyIdsParam: Array<Int>? = null,
        var enemyNumParam: Int? = null,
        var itemIdsParam: Array<Int>? = null,
        var itemNumParam: Int? = null,
        var fieldWidthParam: Int? = null,
        var fieldHeightParam: Int? = null,
        var inherits: FloorInfo? = null
) {

    val enemyIds: Array<Int>
        get() = enemyIdsParam ?: inherits?.enemyIdsParam ?: arrayOf()

    val enemyNum: Int
        get() = enemyNumParam ?: inherits?.enemyNumParam ?: 0

    val itemIds: Array<Int>
        get() = itemIdsParam ?: inherits?.itemIdsParam ?: arrayOf()

    val itemNum: Int
        get() = itemNumParam ?: inherits?.itemNumParam ?: 0

    val fieldWidth: Int
        get() = fieldWidthParam ?: inherits?.fieldWidth ?: 0

    val fieldHeight: Int
        get() = fieldHeightParam ?: inherits?.fieldHeight ?: 0

    companion object {

        /**
         * floorListから指定したフロア情報を取得する。
         * floorListに指定したフロアの情報が存在しない場合は1つずつフロアを下げていく。
         */
        fun getFloorInfo(floor :Int): FloorInfo {
            for (searchFloor in floor downTo 1) {
                var floorInfo = floorList[searchFloor]
                if (floorInfo != null) {
                    if(floorInfo.inherits == null) floorInfo.inherits = getFloorInfo(searchFloor - 1)
                    return floorInfo
                }
            }
            return FloorInfo()
        }
    }

    /**
     * ランダムオブジェクトを渡してランダムに敵の種類のIDを取得する
     * @param random ランダムオブジェクト
     * @throws
     */
    fun getRandomEnemyId(random: Random) :Int {
        val length = this.enemyIds.size
        return this.enemyIds[random.nextInt(length)!!]
    }

    /**
     * ランダムオブジェクトを渡してランダムにアイテムの種類のIDを取得する
     * @param random ランダムオブジェクト
     * @throws
     */
    fun getRandomItemId(random: Random) :Int {
        val length = this.itemIds.size
        return this.itemIds[random.nextInt(length)!!]
    }
}