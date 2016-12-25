package de.lanrena.jeopardy.model

import java.util.*

enum class State {
    Setup,
    Ongoing,
    Finished
}

class Game() {
    val id : UUID
    val players : MutableList<Player>
    var state: State

    init {
        id = UUID.randomUUID()
        players = ArrayList<Player>()
        state = State.Setup
    }

}