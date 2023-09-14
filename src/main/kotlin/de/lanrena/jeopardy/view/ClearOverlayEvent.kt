package de.lanrena.jeopardy.view

import kotlinx.serialization.Serializable

@Serializable
data class ClearOverlayEvent(
    val payload: String = ""
) : JsonMessage {
    override val type = "ClearOverlayEvent"
}
