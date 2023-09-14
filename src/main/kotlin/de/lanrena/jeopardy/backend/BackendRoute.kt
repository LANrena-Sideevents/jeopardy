package de.lanrena.jeopardy.backend

import de.lanrena.jeopardy.controller.JeopardyController
import de.lanrena.jeopardy.view.ClearOverlayEvent
import de.lanrena.jeopardy.view.OverlayEvent
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.streamProvider
import io.ktor.server.application.call
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.thymeleaf.ThymeleafContent
import io.ktor.server.util.getOrFail
import org.koin.ktor.ext.inject
import java.util.*

fun Routing.configureBackend() {
    val jeopardyController by inject<JeopardyController>()

    route("/backend") {
        with(jeopardyController) {
            configureGameOverview()
            getGameData()
            loadGame()
            playerInfoPage()
            updatePlayerData()
            addPlayer()
            listPlayers()
            overlayEvent()
            postAnswer()
            createGame()
        }
        get("/game") {
            call.respondRedirect("/backend")
        }
    }
}

context(JeopardyController)
fun Route.configureGameOverview() {
    get("") {
        val listGames = listGames()
        call.respond(ThymeleafContent("backend/index.html", mapOf("games" to listGames)))
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
                call.respond(ThymeleafContent("backend/game.html", data))
            }

            else -> {
                call.respond(ThymeleafContent("backend/init_game.html", data))
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
            ThymeleafContent(
                "backend/init_game.html", mapOf(
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

context(JeopardyController)
fun Route.addPlayer() {
    post("/game/{gameId}/players") {
        val gameId = call.parameters.getOrFail<UUID>("gameId")
        val gameController = getGameController(gameId)
        if (gameController == null) {
            call.respondRedirect("/backend")
            return@post
        }

        val formParameters = try {
            call.receiveParameters()
        } catch (ex: ContentTransformationException) {
            call.respond<String>(HttpStatusCode.BadRequest, "Error: Missing form-encoded parameters")
            return@post
        }

        val playerName = formParameters.getOrFail("player_name")

        gameController.addPlayer(playerName)
        call.respondRedirect("/backend/game/$gameId/players")
    }
}

context(JeopardyController)
fun Route.listPlayers() {
    get("/game/{gameId}/players") {
        val gameId = call.parameters.getOrFail<UUID>("gameId")
        val gameController = getGameController(gameId)
        if (gameController == null) {
            call.respondRedirect("/backend")
            return@get
        }

        call.respond(
            ThymeleafContent(
                "backend/players.html", mapOf(
                    "game" to gameController.game,
                )
            )
        )
    }
}

context(JeopardyController)
fun Route.overlayEvent() {
    get("/game/{gameId}/field/{col}/{row}") {
        val gameId = call.parameters.getOrFail<UUID>("gameId")
        val gameController = getGameController(gameId)
        if (gameController == null) {
            call.respondRedirect("/backend")
            return@get
        }

        val col = call.parameters.getOrFail<Int>("col")
        val row = call.parameters.getOrFail<Int>("row")

        val fieldController = gameController.getFieldController(col, row)
        if (fieldController == null) {
            call.respondRedirect("/backend")
            return@get
        }

        gameController.sender.send(OverlayEvent(fieldController.field))
        call.respond(
            ThymeleafContent(
                "backend/field.html", mapOf(
                    "game" to gameController.game,
                    "field" to fieldController.field,
                )
            )
        )
    }
}

context(JeopardyController)
fun Route.postAnswer() {
    post("/game/{gameId}/field/{col}/{row}") {
        val gameId = call.parameters.getOrFail<UUID>("gameId")
        val gameController = getGameController(gameId)
        if (gameController == null) {
            call.respondRedirect("/backend")
            return@post
        }

        val col = call.parameters.getOrFail<Int>("col")
        val row = call.parameters.getOrFail<Int>("row")

        val playerId = call.parameters.getOrFail<UUID>("playerId")
        val playerController = gameController.getPlayerController(playerId)
        if (playerController == null) {
            call.respondRedirect("/backend/game/$gameId")
            return@post
        }

        val points = call.parameters.getOrFail<Int>("points")
        val answer = call.parameters.getOrFail("answer")

        var pointsChange = points
        if (answer == "Wrong") {
            pointsChange *= -1
        }

        playerController.updateScore(pointsChange)
        val resolvedPlayer = playerController.player

        val fieldController = gameController.getFieldController(col, row)
        if (fieldController == null) {
            call.respondRedirect("/backend/game/$gameId")
            return@post
        }

        fieldController.markDone(player = resolvedPlayer)

        if (answer == "wrong" && !fieldController.field.bonus) {
            call.respondRedirect("/backend/game/$gameId/field/$col/$row")
        } else {
            call.respondRedirect("/backend/game/$gameId")
        }
    }
}

context(JeopardyController)
fun Route.createGame() {
    post("/game") {
        createNewGame()
        call.respondRedirect("/backend")
    }
}
