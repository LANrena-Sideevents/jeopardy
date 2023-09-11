package de.lanrena.jeopardy.frontend

import de.lanrena.jeopardy.io.WebSocketConnectionManager
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject


fun Application.configureFrontend() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }

    routing {
        get("") {
            call.respond(FreeMarkerContent("frontend/index.ftl", emptyMap<String, String>()))
        }

        val webSocketConnectionManager by inject<WebSocketConnectionManager>()
        webSocket("/jeopardy") {
            webSocketConnectionManager.addConnection(this)
            try {
                for (frame in incoming) {
                    println(frame)
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                webSocketConnectionManager.removeConnection(this)
            }
        }
    }
}
