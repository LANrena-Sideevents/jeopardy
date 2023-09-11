package de.lanrena.jeopardy.view

import kotlinx.serialization.Serializable

@Serializable
class CombinedEvent(
    override val payload: List<JsonMessage<*>>
) : JsonMessage<List<JsonMessage<*>>> {
    override val type = "CombinedEvent"
}
