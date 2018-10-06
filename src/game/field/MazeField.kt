package game.field

import game.datalist.enemyList
import game.datalist.itemList
import game.item.*
import game.mold.FloorInfo
import java.util.*

class MazeField(width: Int, height: Int, floor: Int) : Field(floor, FloorInfo.getFloorInfo(floor)) {

    /**
     * スタートとなる下り階段
     */
    private val start = Stair(isUp = false, field = this)

    /**
     * ゴールとなる上り階段
     */
    private val goal = Stair(isUp = true, field = this)

    /**
     * ランダムオブジェクト
     */
    private val random = Random(floor.toLong())

    init {
        //敵のレベルを設定する。
        val enemyLevel = floor
        //スタートとゴールをランダムに配置する。
        addObjectRandom(start)
        addObjectRandom(goal)
        //壁と矢印マップを生成する。
        createStatueWall()
        arrowMap.generateFieldArrowMap(start.position.x, start.position.y)
        createMazeWall()
        //敵を配置する。
        addEnemiesRandom(enemyLevel)
        //アイテムを配置する。
        addItemsRandom()
    }

    /**
     * 敵キャラクターを配置する。
     */
    private fun addEnemiesRandom(enemyLevel: Int) {
        for (i in 0..floorInfo.enemyNum) {
            val enemyId = floorInfo.getRandomEnemyId(random)
            addObjectRandom(Enemy(mold = enemyList[enemyId]!!, level = enemyLevel, field = this))
        }
    }

    /**
     * アイテムを配置する。
     */
    private fun addItemsRandom() {
        for (i in 0..floorInfo.itemNum) {
            val itemId = floorInfo.getRandomItemId(random)
            addObjectRandom(GameItem(mold = itemList[itemId]!!, field = this))
        }
    }


    /**
     * プレイヤーをスタートに配置する。
     */
    fun setPlayer(player: Player) {
        addObject(start.position.x, start.position.y,  player)
    }

    /**
     *  フィールドの種類が床であり、既に他のオブジェクトが存在しない、かつ距離カウントが存在する（フロアのスタート位置から直接移動可能）
     *  ランダム位置にゲームオブジェクトを追加する。
     *  @param gameObject: GameObject 追加するゲームオブジェクト
     */
    fun addObjectRandom(gameObject: GameObject) {

        while (true) {
            val x = (random.nextInt((width + 1) / 2)) * 2
            val y = (random.nextInt((height + 1) / 2)) * 2
            val fieldBlock = getFieldBlock(x, y)

            if (fieldBlock.type.isFloor && fieldBlock.gameObjects.size == 0) {
                if (!arrowMap.isGenerated || arrowMap.getDistanceCount(x, y) != null) {
                    this.addObject(x, y, gameObject)
                    break
                }
            }
        }
    }

    /**
     * x,yのインデックスが共に奇数になる場所のブロックを壁にする。
     */
    private fun createStatueWall() {
        for (y in 1..(height-1) step 2) {
            for (x in 1..(width-1) step 2) {
                this.getFieldBlock(x, y).type = FieldBlockType.wall
            }
        }
    }

    /**
     * フィールド上にランダムに壁を生成する。
     *  フィールド上のランダムな位置のフィールドブロックを壁に設定していき、その都度 スタートとゴールの間がが移動可能か否かを
     *  矢印マップ(FieldArrowMap)を用いて調べる。 移動不能となった場合は最後に設定した壁を床に戻して生成を終了する。
     */
    private fun createMazeWall() {

        for (i in 0..100) {

            val x = random.nextInt(width)
            val y = random.nextInt(height)

            if ((x + y) % 2 == 1) {
                setFieldBlock(x, y, FieldBlock(FieldBlockType.wall))
            }

            //スタートからゴールへの矢印が繋がらなくなったときは
            if (arrowMap.getDistanceCount(goal.position.x, goal.position.y) == null) {
                setFieldBlock(x, y, FieldBlock(FieldBlockType.floor))
                break
            }
        }
    }

    override fun toString(): String {
        return "floor : ${this.floor} \n${super.toString()}"
    }
}