package game.item

import game.field.Field

class Stair(var isUp: Boolean, field: Field) : GameObject(
        name = if (isUp) "upStair" else "downStair",
        field = field,
        display = if (isUp) "㊤" else "㊦"
) {

    init {
        this.isThroughable = true
    }

    override fun collisionDetected(otherObject: GameObject) {
        if (this.isUp && otherObject is Player) {
            this.field.mapMoveId = this.field.floor + 1
        }
    }

    override fun toString(): String = this.display
}