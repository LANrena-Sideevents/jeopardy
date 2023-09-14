package de.lanrena.jeopardy.view

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ClearOverlayEvent")
data class ClearOverlayEvent(
    val payload: String = ""
) : JsonMessage
