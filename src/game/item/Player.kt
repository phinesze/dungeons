package game.item

import game.field.Field
import game.param.EquipmentState
import game.param.AbilityScore
import kotlin.system.exitProcess

/**
 * プレイヤーキャラクタを表すクラス。GameCharactorを継承する。
 * GameCharactorのopen関数turnをオーバーライドして、行動可能時に対話型メッセージを出力する。
 */
class Player(name: String, var display: String, abilityScore: AbilityScore, var equipmentState: EquipmentState = EquipmentState(), field: Field
) : GameCharactor(name, abilityScore, field) {

    private val turnMessage = """
${name}は何をしますか？
w:前へ  s:後ろへ  a:左へ  d:右へ
n:何もしない
m:マップを表示
p:能力値を表示

q:終了

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

        if (isDead) return

        oneTurn@ while (true) {
            //マップを表示
            println(field.toString())
            //HP・MPの現在値/最大値を表示
            println("HP : ${this.abilityScore.hp}  MP : ${this.abilityScore.mp}")
            //行動可能時のメッセージを表示
            println(turnMessage)
            //入力値を取得してコマンドを実行
            if (doInputAction()) break@oneTurn
        }
    }

    /**
     * プレイヤー自身に入力を求め、入力された値をもとにコマンドを実行する。
     * @return Boolean この入力で行動が終了する場合はtrue、 入力が正しくない場合や行動が終了しないコマンドを入力した場合はfalse
     */
    private fun doInputAction(): Boolean {

        val input = readLine()
        println()

        when (input) {
            "w" -> return moveUpWithMes()
            "s" -> return moveDownWithMes()
            "a" -> return moveLeftWithMes()
            "d" -> return moveRightWithMes()
            "n" -> return true
            "m" -> println(field.toString())
            "p" -> println(this)

            "q" -> {
                println("さよならだ・・また会う日まで")
                exitProcess(0)
            }
            else -> println("もう一度入力してください。")
        }
        return false
    }

    private fun moveAny(x: Int, y: Int, message: String): Boolean {
        println("${name}は${message}に進んだ。")
        return if (tryToMove(x, y)) {
            true
        } else {
            println(moveErrorMessage)
            false
        }
    }

    private fun moveLeftWithMes(): Boolean = moveAny(position.x - 1, position.y, "左")
    private fun moveRightWithMes(): Boolean = moveAny(position.x + 1, position.y, "右")
    private fun moveUpWithMes(): Boolean = moveAny(position.x, position.y - 1, "前")
    private fun moveDownWithMes(): Boolean = moveAny(position.x, position.y + 1, "後ろ")

    override fun collisionDetected(otherObject: GameObject) {
        if (otherObject is Enemy) {
            attackEnemy(otherObject)
        } else if (otherObject is GameItem) {
            //TODO("アイテム取得関連")
        }
    }

    /**
     *  敵キャラクターにダメージを与える。
     *  @param enemy ダメージを受ける敵キャラクター
     */
    private fun attackEnemy(enemy: Enemy) {
        val damage = this.attackTarget(enemy)
        println("${name}は${enemy.name}に${damage}ダメージを与えた")
        if (enemy.abilityScore.hp.now <= 0) println("${enemy.name}を倒した")
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