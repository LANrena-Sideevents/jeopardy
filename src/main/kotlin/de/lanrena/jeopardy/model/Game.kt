package de.lanrena.jeopardy.model

import java.util.*

enum class State {
    Setup,
    Ongoing,
    Finished
}

class Game {
    val id : UUID = UUID.randomUUID()
    val players : MutableList<Player> = ArrayList()
    val categories: MutableList<Category> = ArrayList(5)
    var state: State = State.Setup
}