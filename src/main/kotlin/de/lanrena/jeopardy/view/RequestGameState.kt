package de.lanrena.jeopardy.view

import java.util.UUID

data class RequestGameState(
    val gameId: UUID
) : JsonMessage {
    override val type = "RequestGameState"
}