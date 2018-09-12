package game.item

import com.sun.tools.corba.se.idl.toJavaPortable.Util
import java.lang.reflect.Field

class Stair(var isUp: Boolean) : GameObject(name = if (isUp) "upStair" else "downStair") {

    init {
        this.isThroughable = true
    }

    override fun display(): String {
        return this.toString()
    }

    override fun collisionDetected(otherObject: GameObject) {
        if (otherObject is Player) {
            this.field!!.mapMoveId = this.field!!.floor + 1
        }
    }

    override fun toString(): String = if (isUp) "㊤" else "㊦"
}