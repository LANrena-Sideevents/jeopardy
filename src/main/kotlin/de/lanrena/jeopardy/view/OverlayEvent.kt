package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Field
import kotlinx.serialization.Serializable

@Serializable
data class OverlayEvent(
    val question: String
): JsonMessage {
    constructor(field: Field): this(field.question)

    override val type = "OverlayEvent"
}