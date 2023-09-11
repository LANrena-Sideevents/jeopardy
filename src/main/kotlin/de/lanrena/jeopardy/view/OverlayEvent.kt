package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Field
import kotlinx.serialization.Serializable

@Serializable
data class OverlayEvent(
    override val payload: String
) : JsonMessage<String> {
    constructor(field: Field): this(payload = field.question)

    override val type = "OverlayEvent"
}