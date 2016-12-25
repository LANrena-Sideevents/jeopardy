package de.lanrena.jeopardy.view

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.web.socket.WebSocketMessage

open class JsonMessage(input: JsonObject) : WebSocketMessage<String> {
    protected val json: String

    init {
        this.json = Gson().toJson(input)
    }

    override fun getPayload(): String? = json

    override fun getPayloadLength(): Int = 0

    override fun isLast(): Boolean = true
}
