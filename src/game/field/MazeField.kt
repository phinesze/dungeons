package game.field

import game.item.GameObject
import game.item.Player
import game.item.Stair
import java.util.*

class MazeField(width: Int, height: Int, player: Player, floor: Int) : Field(width, height, floor) {

    /**
     * スタートとなる下り階段
     */
    private val start = Stair(isUp = false)

    /**
     * ゴールとなる上り階段
     */
    private val goal = Stair(isUp = true)

    /**
     * ランダムオブジェクト
     */
    private val random = Random(floor.toLong())

    init {
        addObjectRandom(start)
        addObjectRandom(goal)
        addObject(start.position.x, start.position.y,  player)

        //x,yのインデックスが共に奇数になる場所のブロックを壁にする。
        createStatueWall()
        //指定された位置からの矢印の鎖を生成する。
        arrowMap.createArrowMap(start.position.x, start.position.y)
        //壁を生成する。
        createMazeWall()
    }

    /**
     *  フィールドの種類が床であり、かつ既に他のオブジェクトが存在しないランダム位置にゲームオブジェクトを追加する。
     *  @param gameObject: GameObject 追加するゲームオブジェクト
     */
    fun addObjectRandom(gameObject: GameObject) {

        while (true) {
            val x = (random.nextInt((width + 1) / 2)) * 2
            val y = (random.nextInt((height + 1) / 2)) * 2
            val fieldBlock = getFieldBlock(x, y)

            if (fieldBlock.type.isFloor && fieldBlock.gameObjects.size == 0) {
                this.addObject(x, y, gameObject)
                break
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
     *  矢印マップ(FieldrArrowMap)を用いて調べる。 移動不能となった場合は最後に設定した壁を床に戻して生成を終了する。
     */
    private fun createMazeWall() {

        for (i in 0..100) {

            val x = random.nextInt(width)
            val y = random.nextInt(height)

            if ((x + y) % 2 == 1) {
                setFieldBlock(x, y, FieldBlock(FieldBlockType.wall))
            }

            //スタートからゴールへの矢印が繋がらなくなったときは
            if (arrowMap.getArrowCount(goal.position.x, goal.position.y) == null) {
                setFieldBlock(x, y, FieldBlock(FieldBlockType.floor))
                break
            }
        }
    }
}