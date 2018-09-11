package entry

import game.GameBoard
import game.item.Player
import game.param.AbilityScore
import game.param.MaxNowValue

/**
 * ゲーム開始のエントリーポイント
 */
fun main(args: Array<String>) {

    //ゲーム盤を生成
    val gameBoard = GameBoard()

    //ゲームを開始
    gameBoard.start()
}