package de.lanrena.jeopardy.view

import kotlinx.serialization.Serializable

@Serializable
data class ClearOverlayEvent(
    override val payload: String = ""
) : JsonMessage<String> {
    override val type = "ClearOverlayEvent"
}
