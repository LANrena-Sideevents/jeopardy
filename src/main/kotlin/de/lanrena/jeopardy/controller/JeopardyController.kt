package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.io.WebSocketConnectionManager
import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.view.GameEvent
import de.lanrena.jeopardy.view.JsonMessage
import java.util.*

class JeopardyController(
    private val connectionManager: WebSocketConnectionManager
) {
    private val games: MutableList<Game> =
            Collections.synchronizedList(mutableListOf())

    suspend fun createGame() {
        val element = Game()
        games.add(element)
        connectionManager.send(GameEvent(element))
    }

    fun listGames(): List<Game> = games.filter { !it.finished }

    fun getGameController(id: UUID?): GameController? {
        val game = games.firstOrNull { it.id == id } ?: return null
        return GameController(game, object : MessageSender {
            override suspend fun send(message: JsonMessage) {
                connectionManager.send(message)
            }
        })
    }
}
