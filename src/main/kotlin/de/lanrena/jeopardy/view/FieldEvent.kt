package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Field
import kotlinx.serialization.Serializable

@Serializable
data class SimplifiedField(
    val col: Int,
    val row: Int,
    val disabled: Boolean
)

@Serializable
class FieldEvent(
    override val payload: SimplifiedField
) : JsonMessage<SimplifiedField> {
    constructor(field: Field) : this(
        payload = SimplifiedField(
            col = field.col,
            row = field.row,
            disabled = field.done
        )
    )

    override val type = "FieldEvent"
}
