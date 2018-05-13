package game.item

class Stair(var isUp: Boolean) : GameObject(name = if (isUp) "upStair" else "downStair") {

    override fun display(): String {
        return this.toString()
    }

    override fun toString(): String = if (isUp) "㊤" else "㊦"
}