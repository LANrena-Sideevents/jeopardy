package de.lanrena.jeopardy.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage

@Controller
open class MessageController {
    @MessageMapping("test_message")
    @SendTo("/app/messages")
    fun echo_message(): WebSocketMessage<String> {
        return TextMessage("create_screen_ack asdf")
    }
}
