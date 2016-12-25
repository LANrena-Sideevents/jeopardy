package de.lanrena.jeopardy.view

import org.springframework.web.socket.WebSocketMessage

abstract class JsonMessage() : WebSocketMessage<String> {
    protected var json: String = ""

    override fun getPayload(): String? = json

    override fun getPayloadLength(): Int = 0

    override fun isLast(): Boolean = true
}
