package de.lanrena.jeopardy.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.InputStream
import java.util.*
import java.util.function.Supplier

@Serializable
class Game {
    @Serializable(with = UuidSerializer::class)
    val id: UUID = UUID.randomUUID()
    val players: MutableList<Player> = ArrayList()
    val categories: MutableList<Category> = ArrayList(5)
    val fields: MutableList<Field> = ArrayList(25)
    @Transient
    val resources: MutableMap<String, Supplier<InputStream>> = mutableMapOf()
    var dataLoaded: Boolean = false
    var finished: Boolean = false
}