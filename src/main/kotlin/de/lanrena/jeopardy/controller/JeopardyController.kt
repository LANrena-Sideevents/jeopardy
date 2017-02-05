package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.model.State
import de.lanrena.jeopardy.view.stickyevents.GameEvent
import de.lanrena.jeopardy.view.stickyevents.PlayerEvent
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

    fun listGames(): List<Game> {
        return games.filter { it.state != State.Finished }
    }

    fun findGame(id: UUID): Game? {
        return games.filter { it.id == id }.firstOrNull()
    }

    fun addPlayer(gameId: UUID, name: String, color: String) {
        val player = Player(
                name = name,
                color = color)

        findGame(gameId)?.players?.add(player)
        template?.convertAndSend("/topic/game/" + gameId, PlayerEvent(player))
    }

    fun updatePlayer(game: Game, playerId: UUID,
                     name: String, color: String,
                     points: Int) {

        val player = Player(
                id = playerId,
                name = name,
                color = color,
                points = points)

        game.players.remove(findPlayer(game, playerId))
        game.players.add(player)

        template?.convertAndSend("/topic/game/" + game.id, PlayerEvent(player))
    }

    fun findPlayer(game: Game, playerId: UUID): Player? {
        return game.players.filter { it.id == playerId }.firstOrNull()
    }
}
