package de.lanrena.jeopardy.backend

import de.lanrena.jeopardy.controller.JeopardyController
import de.lanrena.jeopardy.view.ClearOverlayEvent
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
import io.ktor.server.util.getOrFail
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
                playerInfoPage()
                updatePlayerData()
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
        val gameId = call.parameters.getOrFail<UUID>("gameId")
        val gameController = getGameController(gameId)
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
        val gameId = call.parameters.getOrFail<UUID>("gameId")
        val gameController = getGameController(gameId)
        if (gameController == null) {
            call.respondRedirect("/backend")
            return@post
        }

        call.receiveMultipart().readPart()?.let { part ->
            if (part is PartData.FileItem) {
                gameController.loadGameData(part.streamProvider.invoke())
                call.respondRedirect("/backend/game/$gameId")
            }
        }
    }
}

context(JeopardyController)
fun Route.playerInfoPage() {
    get("/game/{gameId}/player/{playerId}") {
        val gameId = call.parameters.getOrFail<UUID>("gameId")
        val gameController = getGameController(gameId)
        if (gameController == null) {
            call.respondRedirect("/backend")
            return@get
        }

        val playerId = call.parameters.getOrFail<UUID>("playerId")
        val playerController = gameController.getPlayerController(playerId)
        if (playerController == null) {
            call.respondRedirect("/backend/game/$gameId")
            return@get
        }

        call.respond(
            FreeMarkerContent(
                "backend/init_game.ftl", mapOf(
                    "game" to gameController.game,
                    "player" to playerController.player
                )
            )
        )
    }
}

context(JeopardyController)
fun Route.updatePlayerData() {
    post("/game/{gameId}/player/{playerId}") {
        val gameId = call.parameters.getOrFail<UUID>("gameId")
        val gameController = getGameController(gameId)
        if (gameController == null) {
            call.respondRedirect("/backend")
            return@post
        }

        val playerId = call.parameters.getOrFail<UUID>("playerId")
        val playerController = gameController.getPlayerController(playerId)
        if (playerController == null) {
            call.respondRedirect("/backend/game/$gameId/player/$playerId")
            return@post
        }

        val playerName = call.parameters.getOrFail("player_name")
        val playerColor = call.parameters.getOrFail("player_color")
        val playerPoints = call.parameters.getOrFail("player_points")

        playerController.updatePlayer(playerName, playerColor, playerPoints.toInt())
        call.respondRedirect("/backend/game/$gameId/player/$playerId")
    }
}
