package de.lanrena.jeopardy.io

import de.lanrena.jeopardy.controller.MessageHandler
import de.lanrena.jeopardy.controller.MessageSender
import de.lanrena.jeopardy.view.JsonMessage
import de.lanrena.jeopardy.view.RequestGameList
import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.util.concurrent.atomic.AtomicInteger

class Connection(
    val session: WebSocketServerSession
) {
    companion object {
        val lastId = AtomicInteger(0)
    }

    val name = "user${lastId.getAndIncrement()}"
}

class WebSocketConnectionManager : MessageSender {
    private val connections = MutableStateFlow(LinkedHashSet<Connection?>())
    private val handlers = MutableStateFlow(LinkedHashSet<MessageHandler>())

    fun addConnection(session: WebSocketServerSession) {
        connections.value = connections.value.also { it.add(Connection(session)) }
    }

    fun removeConnection(session: WebSocketServerSession) {
        connections.value = connections.value.also { it.removeIf { it?.session == session } }
    }

    override suspend fun send(message: JsonMessage) {
        connections.value.forEach {
            it?.session?.sendSerialized(message)
        }
    }

    val json = Json {
        serializersModule = SerializersModule {
            polymorphic(JsonMessage::class) {
                subclass(RequestGameList::class)
            }
        }
    }

    suspend fun receive(payload: String) {
        val message = json.decodeFromString(JsonMessage.serializer(), payload)
        for (handler in handlers.value) {
            if (handler.handleMessage(message)) {
                return
            }
        }

        print("No handler found for $payload")
    }

    fun addHandler(handler: MessageHandler) {
        handlers.value = handlers.value.also { it.add(handler) }
    }
}