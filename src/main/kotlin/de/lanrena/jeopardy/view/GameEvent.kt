package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Game
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class SimplifiedGame(val id: UUID) {
    override fun toString() = "{'id':'$id'}"
}

@Serializable
data class GameEvent(
    override val payload: List<SimplifiedGame>
) : JsonMessage<List<SimplifiedGame>> {
    constructor(vararg listGames: Game) : this(payload = listGames.toList().map { SimplifiedGame(it.id) })

    override val type = "GameEvent"
}