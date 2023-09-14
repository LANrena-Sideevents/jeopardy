package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Field
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("OverlayEvent")
data class OverlayEvent(
    val question: String
): JsonMessage {
    constructor(field: Field): this(field.question)
}