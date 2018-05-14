package game

import game.field.Field
import game.field.MazeField
import game.item.Player

/**
 * ゲームボード
 */
class GameBoard {

    private val field: Field = MazeField(15, 9)
    private val players: MutableList<Player> = mutableListOf()

    /**
     * ゲームボード上の時間経過を開始する。
     * GameObjectの数だけカウント時のメソッドを実行
     */
    fun start() {
        while(true) field.passTime()
    }

    /**
     * プレイヤーをゲーム盤に追加する。
     * @param player プレイヤーオブジェクト
     */
    fun addPlayer(player: Player) {
        players.add(player)
        field.addObject(2, 2, player)
    }
}