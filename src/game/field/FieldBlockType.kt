package game.field

class FieldBlockType (val display: String, val isFloor: Boolean) {

    companion object {
        val floor = FieldBlockType("□", true)
        val wall = FieldBlockType("■", false)
    }

    override fun toString(): String {
        return this.display
    }

}
