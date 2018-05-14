package entry

import game.GameBoard
import game.item.Player
import game.param.AbilityScore
import game.param.MaxNowValue

/**
 * ゲーム開始のエントリーポイント
 */
fun main(args: Array<String>) {

    val gameBoard = GameBoard()

    //プレイヤーの能力値
    val abilityScore = AbilityScore(
            MaxNowValue(50),
            MaxNowValue(40),
            15, 15,
            15,
            15,
            15
    )


    //プレイヤーを生成してゲーム盤に追加
    val player = Player("あなた", "主", abilityScore)
    gameBoard.addPlayer(player)

    //ゲームを開始
    gameBoard.start()
}