package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.view.global.GameListResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import org.springframework.web.socket.WebSocketMessage

@Controller
@Suppress("unused")
open class MessageController(
        @Autowired val jeopardyController: JeopardyController) {

    @SubscribeMapping("/topic/games")
    fun list_games(): WebSocketMessage<String> {
        return GameListResult(*jeopardyController
                .listGames().toTypedArray())
    }
}
