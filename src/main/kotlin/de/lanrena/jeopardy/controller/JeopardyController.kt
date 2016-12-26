package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.model.State
import de.lanrena.jeopardy.view.global.GameListResult
import de.lanrena.jeopardy.view.scoreboard.AddPlayer
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
        games.add(element)
        template?.convertAndSend("/topic/games", GameListResult(element))
    }

    fun listGames(): List<Game> {
        return games.filter { it.state != State.Finished }
    }

    fun findGame(id: UUID): Game? {
        return games.filter { it.id == id }.firstOrNull()
    }

    fun addPlayer(gameId : UUID, name: String, color: String) {
        val player = Player(
                name = name,
                color = color)

        findGame(gameId)?.players?.add(player)
        template?.convertAndSend("/topic/game/" + gameId, AddPlayer(player))
    }
}
