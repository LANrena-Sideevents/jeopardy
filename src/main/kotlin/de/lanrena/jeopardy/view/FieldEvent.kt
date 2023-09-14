package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Field
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimplifiedField(
    val col: Int,
    val row: Int,
    val disabled: Boolean
)

@Serializable
@SerialName("FieldEvent")
data class FieldEvent(
    val payload: SimplifiedField
) : JsonMessage {
    constructor(field: Field) : this(
        payload = SimplifiedField(
            col = field.col,
            row = field.row,
            disabled = field.done
        )
    )
}
