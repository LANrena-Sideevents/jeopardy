package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Player
import kotlinx.serialization.Serializable

@Serializable
data class PlayerEvent(
    val payload: Player
) : JsonMessage
