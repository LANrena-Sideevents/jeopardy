package de.lanrena.jeopardy.view

import kotlinx.serialization.Serializable

@Serializable
data class CombinedEvent(
    val payload: List<JsonMessage>
) : JsonMessage {
    override val type = "CombinedEvent"
}
