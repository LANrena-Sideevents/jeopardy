package de.lanrena.jeopardy.view

import kotlinx.serialization.Serializable

@Serializable
sealed interface JsonMessage<T> {
    val type: String
    val payload: T
}