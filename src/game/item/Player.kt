package game.item

import game.param.AbilityScore
import game.param.EquipmentState
import kotlin.system.exitProcess

class Player(
        name: String,
        var display:
        String, abilityScore: AbilityScore,
        var equipmentState: EquipmentState = EquipmentState()
) : GameCharactor(name, abilityScore) {

    val turnMessage = """
${name}は何をしますか？
w:前へ
s:後ろへ
a:左へ
d:右へ
m:マップを表示
exit:終了

    """

    override fun display(): String = display

    override fun turn() {
        super.turn()

        oneTurn@ while (true) {

            //ターン開始時のメッセージを表示
            println(turnMessage)

            //入力値を取得する。
            val input = readLine()
            println()

            if (doInputAction(input)) break@oneTurn
        }
    }

    private fun doInputAction(input: String?): Boolean {
        when (input) {
            "w" -> {
                println("${name}は前に進んだ。")
                return true
            }
            "s" -> {
                println("${name}は後ろに進んだ。")
                return true
            }
            "a" -> {
                println("${name}は左に進んだ。")
                return true
            }
            "d" -> {
                println("${name}は右に進んだ。")
                return true
            }
            "m" -> {
                println(field.toString())
            }
            "exit" -> {
                println("さよならだ・・また会う日まで")
                exitProcess(0)
            }
            else -> println("もう一度入力してください。")
        }
        return false
    }

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