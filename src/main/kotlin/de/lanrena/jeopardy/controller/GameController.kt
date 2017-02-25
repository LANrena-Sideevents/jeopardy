package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.io.GameDataReader
import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.stickyevents.CategoryEvent
import de.lanrena.jeopardy.view.stickyevents.CombinedEvent
import de.lanrena.jeopardy.view.stickyevents.PlayerEvent
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

class GameController(
        val game: Game,
        val template: SimpMessagingTemplate?) {

    fun addPlayer(name: String) {
        val player = Player(name = name)
        game.players.add(player)
        template?.convertAndSend("/topic/game/$game.id", PlayerEvent(player))
    }

    fun loadGameData(gameDataFile: MultipartFile) {
        val tempFile = File.createTempFile(game.id.toString(), null)
        gameDataFile.transferTo(tempFile)
        tempFile.deleteOnExit()

        val gameDataReader: GameDataReader = GameDataReader(tempFile)

        game.categories.addAll(gameDataReader.categories)
        game.fields.addAll(gameDataReader.fields)
        game.dataLoaded = true

        template?.convertAndSend("/topic/game/$game.id", getCombinedState())
    }

    fun getCombinedState(): CombinedEvent? {
        val initialData: MutableList<de.lanrena.jeopardy.view.JsonMessage> = mutableListOf()
        initialData.addAll(game.players.map(::PlayerEvent))
        initialData.addAll(game.categories.map(::CategoryEvent))
        return CombinedEvent(initialData)
    }

    fun getFieldController(x: Int?, y: Int?): FieldController? {
        val field = game.fields.firstOrNull { it.col == x && it.row == y } ?: return null
        return FieldController(field)
    }

    fun getPlayerController(playerId: UUID?): PlayerController? {
        val player = game.players.filter { it.id == playerId }.firstOrNull() ?: return null
        return PlayerController(player, game, template)
    }
}
