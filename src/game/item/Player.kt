package game.item

import game.param.AbilityScore
import game.param.EquipmentState
import kotlin.system.exitProcess

/**
 * プレイヤーキャラクタを表すクラス。GameCharactorを継承する。
 * GameCharactorのopen関数turnをオーバーライドして、行動可能時に対話型メッセージを出力する。
 */
class Player(
        name: String,
        var display:
        String, abilityScore: AbilityScore,
        var equipmentState: EquipmentState = EquipmentState()
) : GameCharactor(name, abilityScore) {

    private val turnMessage = """
${name}は何をしますか？
w:前へ
s:後ろへ
a:左へ
d:右へ
m:マップを表示
exit:終了

    """

    private val moveErrorMessage = "しかし壁があって動けない"


    override fun display(): String = display

    /**
     * GameCharactorの同名関数をオーバーライドする。
     * 行動可能時になった場合に実行可能なコマンド一覧を表示して、実際の行動をプレイヤー自身にコマンドとして入力させる。
     * 入力したコマンドが有効な場合は実際にコマンドを実行して終了する。
     * 入力が無効な場合または行動が終了しないコマンドの場合は、繰り返し入力させる。
     */
    override fun turn() {

        oneTurn@ while (true) {

            //行動可能時のメッセージを表示
            println(turnMessage)

            //入力値を取得してコマンドを実行
            if (doInputAction()) break@oneTurn
        }
        println("行動終了")
    }

    /**
     * プレイヤー自身に入力を求め、入力された値をもとにコマンドを実行する。
     * @return Boolean この入力で行動が終了する場合はtrue、 入力が正しくない場合や行動が終了しないコマンドを入力した場合はfalse
     */
    private fun doInputAction(): Boolean {

        val input = readLine()
        println()

        when (input) {
            "w" -> return moveUp()
            "s" -> return moveDown()
            "a" -> return moveLeft()
            "d" -> return moveRight()

            "m" -> println(field.toString())

            "exit" -> {
                println("さよならだ・・また会う日まで")
                exitProcess(0)
            }
            else -> println("もう一度入力してください。")
        }
        return false
    }

    private fun moveAny(x: Int, y: Int, message: String): Boolean {
        println("${name}は${message}に進んだ。")
        if (tryToMove(x, y)) {
            return true
        } else {
            println(moveErrorMessage)
            return false
        }
    }

    private fun moveLeft(): Boolean = moveAny(position.x - 1, position.y, "左")

    private fun moveRight(): Boolean = moveAny(position.x + 1, position.y, "右")

    private fun moveUp(): Boolean = moveAny(position.x, position.y - 1, "前")

    private fun moveDown(): Boolean = moveAny(position.x, position.y + 1, "後ろ")

    override fun toString(): String {
        return """
${name}:
能力値:
${abilityScore}
装備:
${equipmentState}
位置:
${position}
        """
    }

}