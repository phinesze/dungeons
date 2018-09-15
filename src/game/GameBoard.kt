package game

import game.field.Field
import game.field.MazeField
import game.item.Player
import game.param.AbilityMold
import game.param.AbilityScore

/**
 * ゲームボードを表す。
 * 追加されたプレイヤーのリストと、
 * プレイヤーや敵などが動き回る場所として「フィールド」オブジェクトを含む。
 */
class GameBoard() {

    //使用するプレイヤー
    private val player: Player = Player(
            "あなた",
            "主",
            AbilityScore(50, 40,
                    AbilityMold(15, 15, 15, 15, 15)
            )
    )

    //使用するフィールド
    private var field: Field = MazeField(15, 9, player, 1)

    /**
     * ゲームボード上の時間経過を開始する。
     * GameObjectの数だけカウント時のメソッドを実行
     */
    fun start() {
        while(true) {
            this.field.count()

            val floor = field.mapMoveId
            if (floor != null)
                field = MazeField(15, 9, player, floor)
        }
    }
}