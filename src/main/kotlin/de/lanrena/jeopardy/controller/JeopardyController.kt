package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.State
import de.lanrena.jeopardy.view.global.GameListResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import java.util.*

class JeopardyController() {

    @Autowired
    private var template: SimpMessagingTemplate? = null

    private val games: MutableList<Game> =
            Collections.synchronizedList(mutableListOf())

    fun createGame() {
        val element = Game()
        template?.convertAndSend("/topic/games", GameListResult(element))
        games.add(element)
    }

    fun listGames(): List<Game> {
        return games.filter { it.state != State.Finished }
    }

    fun findGame(id: UUID): Game? {
        return games.filter { it.id == id }.firstOrNull()
    }
}
