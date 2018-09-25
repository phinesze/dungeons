package game

import game.field.MazeField
import game.item.Player
import game.param.AbilityMold
import game.param.EquipmentState
import kotlin.system.exitProcess

/**
 * ゲームボードを表す。
 * 追加されたプレイヤーのリストと、
 * プレイヤーや敵などが動き回る場所として「フィールド」オブジェクトを含む。
 */
class GameBoard() {

    //使用するフィールド
    private var field: MazeField = MazeField(15, 9, 1)


    private val abilityMold = AbilityMold.generateAbilityMoldMap(
            AbilityMold(
                    50,
                    40,
                    15,
                    15,
                    15,
                    15,
                    15
            ))

    //使用するプレイヤー
    private val player: Player = Player(
            "あなた",
            "主",
            1,
            abilityMold,
            EquipmentState(),
            field
    )

    init {

        field.setPlayer(player)
    }

    /**
     * ゲームボード上の時間経過を開始する。
     * GameObjectの数だけカウント時のメソッドを実行
     */
    fun start() {
        while(true) {
            //フィールドの時間経過
            this.field.count()
            //mapMoveIdがある場合は、新しいフィールドを生成する。
            detectMoveFloor()
            //プレイヤー死亡時に終了
            if (this.player.isDead) exitProcess(0)

        }
    }


    /**
     * mapMoveIdを監視し値がある場合は現在のフィールドを破棄し、新しいフィールドを生成する。
     */
    private fun detectMoveFloor() {
        val floor = field.mapMoveId
        if (floor != null) {
            this.field = MazeField(15, 9, floor)
            this.field.setPlayer(player)
        }
    }
}