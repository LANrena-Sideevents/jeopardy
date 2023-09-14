package de.lanrena.jeopardy.view

import de.lanrena.jeopardy.model.UuidSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RequestGameState(
    @Serializable(with = UuidSerializer::class)
    val gameId: UUID
) : JsonMessage