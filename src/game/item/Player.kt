package game.item

import game.datalist.SKILL_COLD_A
import game.datalist.SKILL_FIRE_A
import game.datalist.SKILL_LIST
import game.field.Field
import game.mold.Skill
import game.param.AbilityMold
import game.param.AbilityScore
import game.param.PlayerLevelAndExperience
import kotlin.system.exitProcess

/**ｓ
 * プレイヤーキャラクタを表すクラス。GameCharacterを継承する。
 * GameCharacterのopen関数turnをオーバーライドして、行動可能時に対話型メッセージを出力する。
 * @property abilityMap 各レベルの値をキーとしたレベルごとの能力値の型のハッシュマップ
 * @property skillMap 各レベルの値をキーとしたレベルごとに覚えるスキルのハッシュマップ
 */
class Player(
        name: String,
        display: String,
        private val abilityMap: Map<Int, AbilityMold<Int>>,
        private val skillMap: Map<Int, Skill>,
        field: Field,
        levelAndExp: PlayerLevelAndExperience
) : GameCharacter(name, display, AbilityScore(abilityMap[levelAndExp.level]!!), field, levelAndExp) {

    /**
     * レベルと累積経験値を表す
     */
    override val levelAndExperience: PlayerLevelAndExperience = levelAndExp

    /**
     * 次のレベルになるまでの必要な経験値
     */
    private val restExp: Long?
        get() = this.levelAndExperience.restExp

    /**
     * スキルの実行のためのキー入力の値をキーとした習得済みのスキルのハッシュマップ
     */
    private val executableSkills: MutableMap<String, Skill> = mutableMapOf(
            "fire-a" to SKILL_LIST[SKILL_FIRE_A]!!,
            "cold-a" to SKILL_LIST[SKILL_COLD_A]!!
    )

    /**
     * 行動可能時に出力されるメッセージ文字列
     */
    private val turnMessage = """
${name}は何をしますか？
w:前へ  s:後ろへ  a:左へ  d:右へ
e:スキル
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
     * @return この入力で行動が終了する場合はtrue、 入力が正しくない場合や行動が終了しないコマンドを入力した場合はfalse
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
            //スキル
            "e" -> return doInputSkill()
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
        println()
        return false
    }

    /**
     * 習得したスキルと実行に必要なキー入力のペアの一覧を表示してスキルの入力を求める。
     * スキルが入力されたされた場合はそのスキルを実行する。
     * @return この入力で行動が終了する場合はtrue、 入力されたスキルが存在しない場合やMPが不足している場合はfalse
     */
    private fun doInputSkill(): Boolean {

        println("スキルを入力してください。")
        for ((keyInput, skill) in this.executableSkills) {
            println("$keyInput:${skill.name}")
        }

        val input = readLine()
        val selectedSkill = executableSkills[input]
        println()

        return if (selectedSkill != null) {
            return if (this.executeSkill(selectedSkill)) {
                true
            } else {
                println("MPが不足しています。"); false
            }
        } else {
            println("入力されたスキルは存在しません。"); false
        }
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
            attackTarget(otherObject)
        } else if (otherObject is GameItem) {
            otherObject.action
        }

    }
    /**
     * メッセージを表示しつつ敵キャラクターにダメージを与える。
     * 敵のHPを0にした場合は倒した際のメッセージを表示して、プレイヤーの経験値を追加する処理を行う。
     * @param target ダメージを受ける敵キャラクター
     */
    override fun attackTarget(target: GameCharacter, skill: Skill): Int {

        val damage = super.attackTarget(target, skill)
        println("${name}は${target.name}に${damage}ダメージを与えた")

        if (target.isDead) {
            val raisedLevel = this.levelAndExperience.addExperience(target.abilityScore.droppingExp.toLong())
            println("${target.name}を倒した")
            println("${target.abilityScore.droppingExp}の経験値を獲得した")

            if (raisedLevel >= 1) {
                refreshAbilityByLevel()
                println("${name}はレベル${this.level}になった")
            }
        }
        return damage
    }

    /**
     * 現在のレベルに応じた能力値に更新する。レベルアップ時に実行される。
     */
    private fun refreshAbilityByLevel() {
        this.abilityScore.abilityMold = this.abilityMap[this.level]!!
    }

    override fun toString(): String {
        return """
$name:
レベル:
$level
次のレベルまで:
$restExp
能力値:
$abilityScore
位置:
$position
        """
    }

}