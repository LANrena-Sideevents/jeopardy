package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.io.GameDataReader
import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.stickyevents.CategoryEvent
import de.lanrena.jeopardy.view.stickyevents.CombinedEvent
import de.lanrena.jeopardy.view.stickyevents.FieldEvent
import de.lanrena.jeopardy.view.stickyevents.PlayerEvent
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream
import java.util.*

class GameController(
        val game: Game,
        val sender: TopicSender) {

    fun addPlayer(name: String) {
        val player = Player(name = name)
        game.players.add(player)
        sender.send(PlayerEvent(player))
    }

    fun loadGameData(gameDataFile: MultipartFile) {
        val tempFile = File.createTempFile(game.id.toString(), null)
        gameDataFile.transferTo(tempFile)
        tempFile.deleteOnExit()

        val gameDataReader: GameDataReader = GameDataReader(tempFile)
        game.categories.addAll(gameDataReader.categories)
        game.fields.addAll(gameDataReader.fields)
        game.resources.putAll(gameDataReader.resources)
        game.dataLoaded = true

        sender.send(getCombinedState())
    }

    fun getCombinedState(): CombinedEvent {
        val initialData: MutableList<de.lanrena.jeopardy.view.JsonMessage> = mutableListOf()
        initialData.addAll(game.players.map(::PlayerEvent))
        initialData.addAll(game.categories.map(::CategoryEvent))
        initialData.addAll(game.fields.map(::FieldEvent))
        return CombinedEvent(initialData)
    }

    fun getFieldController(col: Int?, row: Int?): FieldController? {
        val field = game.fields.firstOrNull { it.col == col && it.row == row } ?: return null
        return FieldController(field, sender)
    }

    fun getPlayerController(playerId: UUID?): PlayerController? {
        val player = game.players.filter { it.id == playerId }.firstOrNull() ?: return null
        return PlayerController(player, game, sender)
    }

    fun resolveResource(resId: String): InputStream? = game.resources.filterKeys { it.startsWith(resId) }.entries.first().value.get()
}
