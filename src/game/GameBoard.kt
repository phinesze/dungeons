package game

import game.field.Field
import game.field.MazeField
import game.item.Player

/**
 * ゲームボードを表す。
 * 追加されたプレイヤーのリストと、
 * プレイヤーや敵などが動き回る場所として「フィールド」オブジェクトを含む。
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
     * プレイヤーをゲーム盤に追加する。プレイヤーはフィールドにオブジェクトの1つとして追加される。
     * @param player プレイヤーオブジェクト
     */
    fun addPlayer(player: Player) {
        players.add(player)
        field.addObject(2, 2, player)
    }
}