package game.field

import game.item.GameObject
import game.item.Stair
import java.util.*

class MazeField(width: Int, height: Int) : Field(width, height) {

    /**
     * スタートとなる下り階段
     */
    private var start = Stair(isUp = false)

    /**
     * ゴールとなる上り階段
     */
    private var goal = Stair(isUp = true)

    /**
     * ランダムオブジェクト
     */
    private val random = Random(5)

    init {
        addObject(2, 2, start)
        addObject(6, 6, goal)

        //x,yのインデックスが共に奇数になる場所のブロックを壁にする。
        createStatueWall()
        //指定された位置からの矢印の鎖を生成する。
        createArrowChain(start.position.x, start.position.y)
        //壁を生成する。
        createMazeWall()

        println(this)
    }

    fun addObjectRandom(gameObject: GameObject) {

        while (true) {
            val x = (random.nextInt((width + 1) / 2)) * 2
            val y = (random.nextInt((height + 1) / 2)) * 2
            val fieldBlock = getFieldBlock(x, y)

            if (fieldBlock.type == FieldBlockType.floor && fieldBlock.gameObjects.size == 0) {
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
     * 壁を生成する。
     */
    private fun createMazeWall() {

        for (i in 0..100) {

            val x = random.nextInt(width)
            val y = random.nextInt(height)

            if ((x + y) % 2 == 1) {
                setFieldBlock(x, y, FieldBlock(FieldBlockType.wall))
            }

            //スタートからゴールへの矢印が繋がらなくなったときは
            if (arrowLayer.getArrowCount(goal.position.x, goal.position.y) == null) {
                setFieldBlock(x, y, FieldBlock(FieldBlockType.floor))
                break
            }
        }
    }
}