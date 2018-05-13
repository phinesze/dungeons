package game.field

enum class FieldBlockType {
    floor, wall;

    companion object {
        private val str = mapOf(
            floor to "□",
            wall to "■"
        )
    }

    override fun toString(): String {
        return str[this] ?: "  "
    }
}