package de.lanrena.jeopardy.backend

import de.lanrena.jeopardy.controller.JeopardyController
import de.lanrena.jeopardy.view.ClearOverlayEvent
import io.ktor.http.Parameters
import io.ktor.http.content.PartData
import io.ktor.http.content.streamProvider
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureBackend() {
    routing {
        val jeopardyController by inject<JeopardyController>()

        route("/backend") {
            with(jeopardyController) {
                configureGameOverview()
                getGameData()
                loadGame()
                updatePlayer()
            }
            get("/game") {
                call.respondRedirect("/backend")
            }
        }
    }
}

context(JeopardyController)
fun Route.configureGameOverview() {
    get("") {
        call.respond(FreeMarkerContent("backend/index.ftl", mapOf("games" to listGames())))
    }
}

context(JeopardyController)
fun Route.getGameData() {
    get("/game/{gameId}") {
        val gameController = getGameController(call.parameters.gameId)
        if (gameController == null) {
            call.respondRedirect("/backend")
            return@get
        }

        val data = mapOf(
            "game" to gameController.game,
            "gamecontroller" to gameController
        )

        when {
            gameController.game.dataLoaded -> {
                gameController.sender.send(ClearOverlayEvent())
                call.respond(FreeMarkerContent("backend/game.ftl", data))
            }
            else -> {
                call.respond(FreeMarkerContent("backend/init_game.ftl", data))
            }
        }
    }
}

context(JeopardyController)
fun Route.loadGame() {
    post("/game/{gameId}/load") {
        val gameId = call.parameters.gameId
        val gameController = getGameController(gameId)
        if (gameController == null) {
            call.respondRedirect("/backend")
            return@post
        }

        call.receiveMultipart().readPart()?.let  { part ->
            if (part is PartData.FileItem) {
                gameController.loadGameData(part.streamProvider.invoke())
                call.respondRedirect("/backend/game/$gameId")
            }
        }
    }
}

fun Route.updatePlayer() {
    get("/game/{gameId}/player/{playerId}") {

    }
}

private val Parameters.gameId: UUID?
    get() {
        val gameIdParameter = this["gameId"]
        return gameIdParameter?.let {
            try {
                UUID.fromString(it)
            } catch (ex: IllegalArgumentException) {
                null
            }
        }
    }
