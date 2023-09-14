package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.UuidSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class SimplifiedGame(
    @Serializable(with = UuidSerializer::class) val id: UUID
)

@Serializable
data class GameEvent(
    val payload: List<SimplifiedGame>
) : JsonMessage {
    constructor(vararg listGames: Game) : this(payload = listGames.toList().map { SimplifiedGame(it.id) })

    override val type = "GameEvent"
}