package game.item
import game.field.Field
import game.param.*
import kotlin.system.exitProcess

/**ｓ
 * プレイヤーキャラクタを表すクラス。GameCharacterを継承する。
 * GameCharacterのopen関数turnをオーバーライドして、行動可能時に対話型メッセージを出力する。
 */
class Player(
        name: String,
        display: String,
        abilityMap: Map<Int, AbilityMold>,
        var equipmentState: EquipmentState = EquipmentState(),
        field: Field,
        levelAndExp: PlayerLevelAndExperience
) : GameCharacter(name, display, AbilityScore(abilityMap[levelAndExp.level]!!), field, levelAndExp) {

    /**
     * レベルと累積経験値を表す
     */
    override val levelAndExperience: PlayerLevelAndExperience = levelAndExp

    /**
     * 各レベルごとの能力値の型
     */
    val abilityMap: Map<Int, AbilityMold> = abilityMap

    /**
     * 行動可能時に出力されるメッセージ文字列
     */
    private val turnMessage = """
${name}は何をしますか？
w:前へ  s:後ろへ  a:左へ  d:右へ
x:待機
m:マップを表示
p:能力値を表示

q:ゲームを終了
    """


    /**
     * MoveAnyを実行した際に壁に阻まれた際に表示されるメッセージ
     */
    private val moveErrorMessage = "しかし壁があって動けない"

    init {
        this.timeWait = TIME_WAIT_START / 2
    }

    /**
     * GameCharacterの同名関数をオーバーライドする。
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
            println("Lv : ${this.level} HP : ${this.abilityScore.hp}  MP : ${this.abilityScore.mp}")
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
            //上へ移動
            "w" -> return moveUpWithMessage()
            //下へ移動
            "s" -> return moveDownWithMessage()
            //左へ移動
            "a" -> return moveLeftWithMessage()
            //右へ移動
            "d" -> return moveRightWithMessage()
            //待機
            "x" -> return true
            //マップを表示
            "m" -> println(field.toString())
            //能力値を表示
            "p" -> println(this)
            //ゲームを終了
            "q" -> {
                println("さよならだ・・また会う日まで")
                exitProcess(0)
            }
            //入力失敗
            else -> println("もう一度入力してください。")
        }
        return false
    }

    /**
     * 上下左右いずれかへの移動がコマンドとして入力された場合に「(プレイヤー名)は(上|下|左|右)に進んだ。」というメッセージとともに移動を試みる。
     * @return 移動先のフィールドブロックが床である場合(別のゲームオブジェクト重なった場合も)はtrue、壁であり移動不可能な場合はfalse
     */
    private fun moveAnyWithMessage(x: Int, y: Int, message: String): Boolean {
        println("${name}は${message}に進んだ。")
        return if (tryToMove(x, y)) {
            true
        } else {
            println(moveErrorMessage)
            false
        }
    }

    /**
     * 「左へ移動」が入力された場合にメッセージとともに左に移動する。
     * @return 移動先のフィールドブロックが床であり移動可能な場合はtrue、壁であり移動不可能な場合はfalse
     */
    private fun moveLeftWithMessage(): Boolean = moveAnyWithMessage(position.x - 1, position.y, "左")

    /**
     * 「右へ移動」が入力された場合にメッセージとともに右に移動する。
     * @return 移動先のフィールドブロックが床であり移動可能な場合はtrue、壁であり移動不可能な場合はfalse
     */
    private fun moveRightWithMessage(): Boolean = moveAnyWithMessage(position.x + 1, position.y, "右")

    /**
     * 「上へ移動」が入力された場合にメッセージとともに上に移動する。
     * @return 移動先のフィールドブロックが床であり移動可能な場合はtrue、壁であり移動不可能な場合はfalse
     */
    private fun moveUpWithMessage(): Boolean = moveAnyWithMessage(position.x, position.y - 1, "前")

    /**
     * 「下へ移動」が入力された場合にメッセージとともに下に移動する。
     * @return 移動先のフィールドブロックが床であり移動可能な場合はtrue、壁であり移動不可能な場合はfalse
     */
    private fun moveDownWithMessage(): Boolean = moveAnyWithMessage(position.x, position.y + 1, "後ろ")

    /**
     * プレイヤーがほかの敵キャラクターまたはアイテムと位置が重なった（衝突した）場合の処理を行う。
     * 敵キャラクターに衝突した場合はその敵キャラクターにダメージを与える処理を行う。
     * @param otherObject 重なった（衝突した）相手側のゲームオブジェクト
     */
    override fun collisionDetected(otherObject: GameObject) {
        if (otherObject is Enemy) {
            attackEnemy(otherObject)
        } else if (otherObject is GameItem) {
            //TODO("アイテム取得関連")
        }
    }

    /**
     * メッセージを表示しつつ敵キャラクターにダメージを与える。
     * 敵のHPを0にした場合は倒した際のメッセージを表示して、プレイヤーの経験値を追加する処理を行う。
     * @param enemy ダメージを受ける敵キャラクター
     */
    private fun attackEnemy(enemy: Enemy) {
        val damage = this.attackTarget(enemy)
        println("${name}は${enemy.name}に${damage}ダメージを与えた")

        if (enemy.abilityScore.hp.now <= 0) {
            val raisedLevel = this.levelAndExperience.addExperience(enemy.experience)
            println("${enemy.name}を倒した")
            println("${enemy.experience}の経験値を獲得した")

            if (raisedLevel >= 1) {
                refleshAbilityByLevel()
                println("${name}はレベル${this.level}になった")
            }
        }
    }

    /**
     * 現在のレベルに応じた能力値に更新する。レベルアップ時に実行される。
     */
    private fun refleshAbilityByLevel() {
        this.abilityScore.abilityMold = this.abilityMap[this.level]!!
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