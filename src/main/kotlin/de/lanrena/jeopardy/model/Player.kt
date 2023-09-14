package de.lanrena.jeopardy.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Player(
    @Serializable(with = UuidSerializer::class)
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var points: Int = 0,
    var color: String = Color.randomColor().value
)
