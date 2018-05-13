package game

import game.field.Field
import game.field.MazeField
import game.item.GameObject

class GameBoard() {

    private val field: Field = MazeField(15, 9)
    var gameObjects: List<GameObject> = listOf()
    var count: Int = 0

    /**
     * ゲームボード上の時間経過を開始する。
     * GameObjectの数だけカウント時のメソッドを実行
     */
    fun start() {
        count = 0;
        while(true) {
            for (obj in gameObjects) { obj.moveInCount() }
            count += 1
        }
    }
}