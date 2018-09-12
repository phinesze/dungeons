package entry

import game.GameBoard

/**
 * ゲーム開始のエントリーポイント
 */
fun main(args: Array<String>) {

    //ゲーム盤を生成
    val gameBoard = GameBoard()

    //ゲームを開始
    gameBoard.start()
}