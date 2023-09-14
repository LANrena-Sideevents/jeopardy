package de.lanrena.jeopardy.view

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CombinedEvent")
data class CombinedEvent(
    val payload: List<JsonMessage>
) : JsonMessage