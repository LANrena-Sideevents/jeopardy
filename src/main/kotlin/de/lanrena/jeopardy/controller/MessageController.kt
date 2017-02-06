package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.view.stickyevents.GameEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
@Suppress("unused")
open class MessageController(@Autowired val jeopardyController: JeopardyController) {

    @SubscribeMapping("/topic/games")
    fun list_games(): Any = GameEvent(*jeopardyController.listGames().toTypedArray())

    @SubscribeMapping("/topic/game/{gameid}")
    fun subscribe_game(@DestinationVariable("gameid") gameId: UUID): Any? =
            jeopardyController.getCombinedState(gameId)
}
