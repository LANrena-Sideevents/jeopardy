package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Player
import kotlinx.serialization.Serializable

@Serializable
data class PlayerEvent(
    override val payload: Player
) : JsonMessage<Player> {
    override val type = "PlayerEvent"
}
