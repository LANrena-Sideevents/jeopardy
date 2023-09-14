package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoryEvent(
    val payload: Category
) : JsonMessage {
    override val type = "CategoryEvent"
}
