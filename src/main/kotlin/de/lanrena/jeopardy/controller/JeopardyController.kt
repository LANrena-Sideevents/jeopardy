package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.view.stickyevents.GameEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import java.util.*

class JeopardyController {
    @Autowired
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
        val game = games.filter { it.id == id }.firstOrNull() ?: return null
        return GameController(game, GameTopicSender(template, game))
    }
}

private class GameTopicSender(
        val template: SimpMessagingTemplate?,
        val game: Game) : TopicSender {

    override fun send(message: Any) {
        template?.convertAndSend("/topic/game/${game.id}", message)
    }
}
