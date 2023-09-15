package de.lanrena.jeopardy.frontend

import de.lanrena.jeopardy.controller.JeopardyController
import de.lanrena.jeopardy.io.WebSocketConnectionManager
import de.lanrena.jeopardy.view.RequestGameList
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respond
import io.ktor.server.response.respondOutputStream
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.thymeleaf.ThymeleafContent
import io.ktor.server.util.getOrFail
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.FrameType
import io.ktor.websocket.readText
import org.koin.ktor.ext.inject
import java.util.*

fun Routing.configureFrontend() {
    staticResources("/css", "css")
    staticResources("/js", "js")
    staticResources("/fonts", "fonts")

    get("") {
        call.respond(ThymeleafContent("frontend/index.html", emptyMap<String, String>()))
    }

    val jeopardyController by inject<JeopardyController>()
    with(jeopardyController) {
        configureResourceRoute()
    }

    val webSocketConnectionManager by inject<WebSocketConnectionManager>()
    with(webSocketConnectionManager) {
        configureWebsocket()
    }
}

context(JeopardyController)
fun Route.configureResourceRoute() {
    get("/resource/{gameId}/{resId}") {
        val gameId = call.parameters.getOrFail<UUID>("gameId")
        val gameController = getGameController(gameId)
        if (gameController == null) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }

        val resId = call.parameters.getOrFail("resId")
        val res = gameController.resolveResource(resId)
        call.respondOutputStream { res.transferTo(this) }
    }
}

context(WebSocketConnectionManager)
fun Route.configureWebsocket() {
    webSocket("/jeopardy") {
        addConnection(this)
        try {
            for (frame in incoming) {
                when (frame.frameType) {
                    FrameType.TEXT -> receive((frame as Frame.Text).readText())
                    else -> continue
                }
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
        } finally {
            removeConnection(this)
        }
    }
}
