package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class SimplifiedGame(
    @Serializable(with = UuidSerializer::class) val id: UUID
)

@Serializable
@SerialName("GameEvent")
data class GameEvent(
    val games: List<SimplifiedGame>
) : JsonMessage {
    constructor(vararg listGames: Game) : this(games = listGames.toList().map { SimplifiedGame(it.id) })
}