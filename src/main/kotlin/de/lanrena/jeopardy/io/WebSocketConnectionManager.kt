package de.lanrena.jeopardy.io

import de.lanrena.jeopardy.controller.TopicSender
import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.atomic.AtomicInteger

class Connection(
    val session: WebSocketServerSession
) {
    companion object {
        val lastId = AtomicInteger(0)
    }

    val name = "user${lastId.getAndIncrement()}"
}

class WebSocketConnectionManager: TopicSender {
    private val connections = MutableStateFlow(LinkedHashSet<Connection?>())

    fun addConnection(session: WebSocketServerSession) {
        connections.value = connections.value.also { it.add(Connection(session)) }
    }

    fun removeConnection(session: WebSocketServerSession) {
        connections.value = connections.value.also { it.removeIf { it?.session == session } }
    }

    override suspend fun send(message: Any) {
        connections.value.forEach {
            it?.session?.sendSerialized(message)
        }
    }
}