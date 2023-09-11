package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.view.GameEvent
import java.util.*

@Suppress("unused")
class MessageController(
    private val jeopardyController: JeopardyController
) {
    @SubscribeMapping("/topic/games")
    fun listGames(): Any = GameEvent(*jeopardyController.listGames().toTypedArray())

    @SubscribeMapping("/topic/game/{gameId}")
    fun subscribeGame(@DestinationVariable("gameId") gameId: UUID): Any? =
        jeopardyController.getGameController(gameId)?.getCombinedState()
}
