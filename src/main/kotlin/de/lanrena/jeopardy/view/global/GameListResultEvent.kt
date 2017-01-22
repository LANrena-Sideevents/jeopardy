package de.lanrena.jeopardy.view.global

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.view.JsonMessage
import java.util.*

class SimplifiedGame(val id: UUID) {
    override fun toString(): String = "{'id':'" + this.id + "'}"
}

class GameListResultEvent(vararg listGames: Game)
    : JsonMessage(
        type = "GameListResultEvent",
        payload = listGames.map { SimplifiedGame(it.id) })