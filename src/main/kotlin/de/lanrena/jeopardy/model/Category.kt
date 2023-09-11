package de.lanrena.jeopardy.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val column: Int,
    val label: String
)
