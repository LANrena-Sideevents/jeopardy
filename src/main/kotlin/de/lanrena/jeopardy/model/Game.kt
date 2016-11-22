package de.lanrena.jeopardy.model

import java.util.*

data class Game (
        val id : UUID,
        val players : List<Player>)