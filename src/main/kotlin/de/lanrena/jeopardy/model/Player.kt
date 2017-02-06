package de.lanrena.jeopardy.model

import java.util.*

data class Player(
        val id: UUID = UUID.randomUUID(),
        val name: String,
        var points: Int = 0,
        val color: String = Color.randomColor().value)
