package game.mold

import game.datalist.floorList
import java.util.*

/**
 * フロアごとの情報を表す
 * @property enemyIds 出現する敵の種類のIDの配列、enemyListのキーの値を表す
 * @property fieldWidth フロアの幅を表す数値
 * @property fieldHeight フロアの高さを表す数値
 */
class FloorInfo(
        var enemyIds : Array<Int>? = null,
        var fieldWidth: Int? = null,
        var fieldHeight: Int? = null
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
     * ランダムオブジェクトを渡してランダム敵の種類のIDを取得する
     * @param random ランダムオブジェクト
     * @throws
     */
    fun getRandomEnemyId(random: Random) :Int {
        val length = this.enemyIds!!.size
        return this.enemyIds!![random.nextInt(length)!!]
    }
}