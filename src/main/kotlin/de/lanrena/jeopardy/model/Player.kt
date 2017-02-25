package de.lanrena.jeopardy.model

import java.util.*

data class Player(
        val id: UUID = UUID.randomUUID(),
        var name: String,
        var points: Int = 0,
        var color: String = Color.randomColor().value)
