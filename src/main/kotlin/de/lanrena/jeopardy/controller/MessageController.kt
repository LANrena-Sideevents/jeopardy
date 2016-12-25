package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.view.global.GameListResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.socket.WebSocketMessage

@Controller
open class MessageController(
        @Autowired val jeopardyController: JeopardyController) {

    @MessageMapping("list_games")
    @SendTo("/topic/games")
    fun list_games(): WebSocketMessage<String> {
        return GameListResult(*jeopardyController
                .listGames().toTypedArray())
    }
}
