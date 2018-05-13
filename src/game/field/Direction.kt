package game.field

enum class Direction {

    left, right, top, bottom;

    companion object {
        private val directionToArrow = mapOf(
                left to Arrow.left,
                right to Arrow.right,
                top to Arrow.top,
                bottom to Arrow.bottom
        )
    }


    fun toArrow(): Arrow {
        return directionToArrow[this]!!
    }
}