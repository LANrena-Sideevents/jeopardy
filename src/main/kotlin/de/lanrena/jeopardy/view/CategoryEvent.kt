package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Category
import kotlinx.serialization.Serializable

@Serializable
class CategoryEvent(
    override val payload: Category
) : JsonMessage<Category> {
    override val type = "CategoryEvent"
}
