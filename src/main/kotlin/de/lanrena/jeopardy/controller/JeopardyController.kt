package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.io.GameDataReader
import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.stickyevents.CategoryEvent
import de.lanrena.jeopardy.view.stickyevents.CombinedEvent
import de.lanrena.jeopardy.view.stickyevents.GameEvent
import de.lanrena.jeopardy.view.stickyevents.PlayerEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.multipart.MultipartFile
import java.io.File
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

    fun findGame(id: UUID): Game? = games.filter { it.id == id }.firstOrNull()

    fun addPlayer(gameId: UUID, name: String, color: String) {
        val player = Player(
                name = name,
                color = color)

        findGame(gameId)?.players?.add(player)

        template?.convertAndSend("/topic/game/$gameId", PlayerEvent(player))
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

        template?.convertAndSend("/topic/game/$game.id", PlayerEvent(player))
    }

    fun findPlayer(game: Game, playerId: UUID): Player? =
            game.players.filter { it.id == playerId }.firstOrNull()

    fun loadGameData(gameId: UUID, gameDataFile: MultipartFile) {
        val tempFile = File.createTempFile(gameId.toString(), null)
        gameDataFile.transferTo(tempFile)
        tempFile.deleteOnExit()

        val gameDataReader: GameDataReader = GameDataReader(tempFile)

        val game: Game = findGame(gameId) ?: return
        game.categories.addAll(gameDataReader.categories)
        game.dataLoaded = true

        template?.convertAndSend("/topic/game/$gameId", getCombinedState(gameId))
    }

    fun getCombinedState(gameId: UUID): CombinedEvent? {
        val game: Game = findGame(gameId) ?: return null
        val initialData: MutableList<de.lanrena.jeopardy.view.JsonMessage> = mutableListOf()
        initialData.addAll(game.players.map(::PlayerEvent))
        initialData.addAll(game.categories.map(::CategoryEvent))
        return CombinedEvent(initialData)
    }
}
