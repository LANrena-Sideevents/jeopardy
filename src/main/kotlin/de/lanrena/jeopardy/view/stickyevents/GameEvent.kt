package de.lanrena.jeopardy.view.stickyevents

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.view.JsonMessage
import java.util.*

class GameEvent(vararg listGames: Game)
    : JsonMessage(
        type = "GameEvent",
        payload = listGames.map { SimplifiedGame(it.id) }) {

    class SimplifiedGame(val id: UUID) {
        override fun toString(): String = "{'id':'" + this.id + "'}"
    }
}