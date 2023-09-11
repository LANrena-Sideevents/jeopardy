package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.view.GameEvent
import org.springframework.messaging.simp.SimpMessagingTemplate
import java.util.*

class JeopardyController {
    private var template: SimpMessagingTemplate? = null

    private val games: MutableList<Game> =
            Collections.synchronizedList(mutableListOf())

    fun createGame() {
        val element = Game()
        games.add(element)
        template?.convertAndSend("/topic/games", GameEvent(element))
    }

    fun listGames(): List<Game> = games.filter { !it.finished }

    fun getGameController(id: UUID?): GameController? {
        val game = games.firstOrNull { it.id == id } ?: return null
        return GameController(game, object : TopicSender {
            override fun send(message: Any): Unit? =
                    template?.convertAndSend("/topic/game/${game.id}", message)
        })
    }
}
