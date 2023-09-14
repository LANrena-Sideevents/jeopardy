package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CategoryEvent")
data class CategoryEvent(
    val payload: Category
) : JsonMessage
