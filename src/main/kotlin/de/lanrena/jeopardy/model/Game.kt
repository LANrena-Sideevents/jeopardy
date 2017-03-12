package de.lanrena.jeopardy.model

import java.io.InputStream
import java.util.*
import java.util.function.Supplier

class Game {
    val id: UUID = UUID.randomUUID()
    val players: MutableList<Player> = ArrayList()
    val categories: MutableList<Category> = ArrayList(5)
    val fields: MutableList<Field> = ArrayList(25)
    val resources: MutableMap<String, Supplier<InputStream>> = mutableMapOf()
    var dataLoaded: Boolean = false
    var finished: Boolean = false
}