package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Player
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PlayerEvent")
data class PlayerEvent(
    val payload: Player
) : JsonMessage
