package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Game
import java.util.*

class JeopardyController() {
    private val games : MutableList<Game> =
            Collections.synchronizedList(mutableListOf())

    fun createGame() {
        games.add(Game())
    }

    fun listGames(): List<Game> {
        return games
    }
}
