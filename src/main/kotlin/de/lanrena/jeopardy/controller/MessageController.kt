package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.view.JsonMessage
import de.lanrena.jeopardy.view.global.CombinedEvent
import de.lanrena.jeopardy.view.global.GameListResultEvent
import de.lanrena.jeopardy.view.scoreboard.PlayerEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
@Suppress("unused")
open class MessageController(
        @Autowired val jeopardyController: JeopardyController) {

    @SubscribeMapping("/topic/games")
    fun list_games(): Any {
        return GameListResultEvent(*jeopardyController
                .listGames().toTypedArray())
    }

    @SubscribeMapping("/topic/game/{gameid}")
    fun subscribe_game(@DestinationVariable("gameid") gameId: UUID): Any? {
        val game: Game = jeopardyController.findGame(gameId) ?: return null

        val initialData: MutableList<JsonMessage> = mutableListOf()
        initialData.addAll(game.players.map(::PlayerEvent))

        return CombinedEvent(initialData)
    }
}
