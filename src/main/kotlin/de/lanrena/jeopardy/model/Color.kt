package de.lanrena.jeopardy.model

/**
 * SNES Color palette according to https://en.wikipedia.org/wiki/List_of_video_game_console_palettes#Super_NES_.28SNES.29
 */
enum class Color(val value: String) {
    Blue("#0078f8"),
    Purple("#6844fc"),
    Pink("#d800cc"),
    Red("#e40058"),
    Orange("#f83800"),
    Green("#00b800"),
    Lime("#b8f818"),
    ;

    override fun toString(): String = value

    companion object {
        fun randomColor() = entries.random()
    }
}