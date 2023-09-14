package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.io.GameDataReader
import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.CategoryEvent
import de.lanrena.jeopardy.view.CombinedEvent
import de.lanrena.jeopardy.view.FieldEvent
import de.lanrena.jeopardy.view.PlayerEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.*

class GameController(
    val game: Game,
    val sender: MessageSender
) {

    suspend fun addPlayer(name: String) {
        val player = Player(name = name)
        game.players.add(player)
        sender.send(PlayerEvent(player))
    }

    suspend fun loadGameData(gameDataStream: InputStream) {
        withContext(Dispatchers.IO) {
            val gameDataReader = GameDataReader(gameDataStream)
            game.categories.addAll(gameDataReader.categories)
            game.fields.addAll(gameDataReader.fields)
            game.resources.putAll(gameDataReader.resources)
            game.dataLoaded = true
        }

        sender.send(getCombinedState())
    }

    fun getCombinedState(): CombinedEvent {
        val initialData = listOf(
            game.players.map(::PlayerEvent),
            game.categories.map(::CategoryEvent),
            game.fields.map(::FieldEvent)
        )
        return CombinedEvent(initialData.flatten())
    }

    fun getFieldController(col: Int?, row: Int?): FieldController? {
        val field = game.fields.firstOrNull {
            it.col == col && it.row == row
        } ?: return null

        return FieldController(field, sender)
    }

    fun getPlayerController(playerId: UUID?): PlayerController? {
        val player = game.players.firstOrNull { it.id == playerId } ?: return null
        return PlayerController(player, game, sender)
    }

    fun resolveResource(resId: String): InputStream =
        game.resources
            .filterKeys { it.startsWith(resId) }
            .entries
            .first()
            .value
            .get()
}
