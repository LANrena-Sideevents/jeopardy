package de.lanrena.jeopardy.model

import java.util.*

class Game {
    val id: UUID = UUID.randomUUID()
    val players: MutableList<Player> = ArrayList()
    val categories: MutableList<Category> = ArrayList(5)
    val fields: MutableList<Field> = ArrayList(25)
    var dataLoaded: Boolean = false
    var finished: Boolean = false
}