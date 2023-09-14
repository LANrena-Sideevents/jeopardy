package de.lanrena.jeopardy.view

import kotlinx.serialization.Serializable

@Serializable
data object RequestGameList : JsonMessage {
    override val type = "RequestGameList"
}