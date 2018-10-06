package game.mold

import game.datalist.floorList
import java.util.*

/**
 * フロアごとの情報を表す
 * @property enemyIds フロアに出現する敵の種類のIDの配列、enemyListのキーの値を表す
 * @property enemyNum フロアに出現する敵の数
 * @property fieldWidth フロアの幅を表す数値
 * @property fieldHeight フロアの高さを表す数値
 */
class FloorInfo(
        var enemyIds: Array<Int> = arrayOf(),
        var enemyNum: Int = 0,
        var itemIds: Array<Int> = arrayOf(),
        var itemNum: Int = 0,
        var fieldWidth: Int = 10,
        var fieldHeight: Int = 10
) {
    companion object {

        /**
         * floorListから指定したフロア情報を取得する。
         * floorListに指定したフロアの情報が存在しない場合は1つずつフロアを下げていく。
         */
        fun getFloorInfo(floor :Int): FloorInfo {
            for (searchFloor in floor downTo 1) {
                var floorList = floorList[searchFloor]
                if (floorList != null) return floorList
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
        val length = this.enemyIds!!.size
        return this.enemyIds!![random.nextInt(length)!!]
    }

    /**
     * ランダムオブジェクトを渡してランダムにアイテムの種類のIDを取得する
     * @param random ランダムオブジェクト
     * @throws
     */
    fun getRandomItemId(random: Random) :Int {
        val length = this.itemIds!!.size
        return this.itemIds!![random.nextInt(length)!!]
    }
}