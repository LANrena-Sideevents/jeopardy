package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.ClearOverlayEvent
import de.lanrena.jeopardy.view.OverlayEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*


open class BackendController() {

    @GetMapping("/game/{gameId}/player/{playerId}")
    fun editPlayerForm(
        @PathVariable("gameId") gameId: UUID?,
        @PathVariable("playerId") playerId: UUID?,
        model: Model): String {

        val gameController = gameState.getGameController(gameId)
            ?: return "redirect:/backend/index"

        val playerController = gameController.getPlayerController(playerId)
            ?: return "redirect:/backend/game/$gameId"

        model.addAttribute("game", gameController.game)
        model.addAttribute("player", playerController.player)
        return "backend/editplayer"
    }

    @PostMapping("/game/{gameId}/player/{playerId}")
    fun editPlayerAction(
            @PathVariable("gameId") gameId: UUID?,
            @PathVariable("playerId") playerId: UUID?,
            @RequestParam("player_name") player_name: String,
            @RequestParam("player_color") player_color: String,
            @RequestParam("player_points") player_points: String,
            model: Model): String {

        val gameController = gameState.getGameController(gameId)
                ?: return "redirect:/backend/index"

        val playerController = gameController.getPlayerController(playerId)
                ?: return "redirect:/backend/game/$gameId/player/$playerId"

        playerController.updatePlayer(player_name, player_color, player_points.toInt())
        model.addAttribute("game", gameController.game)
        return "redirect:/backend/game/$gameId/players"
    }

    @PostMapping("/game/{gameId}/players")
    fun addPlayer(@PathVariable("gameId") gameId: UUID,
                  @RequestParam("player_name") player_name: String): String {

        val gameController = gameState.getGameController(gameId)
                ?: return "redirect:/backend/index"

        gameController.addPlayer(player_name)
        return "redirect:/backend/game/$gameId/players"
    }

    @GetMapping("/game/{gameId}/players")
    fun listPlayers(@PathVariable("gameId") gameId: UUID?,
                    model: Model): String {

        val gameController = gameState.getGameController(gameId)
                ?: return "redirect:/backend/index"

        model.addAttribute("game", gameController.game)
        return "backend/players"
    }

    @GetMapping("/game/{gameId}/field/{col}/{row}")
    fun displayField(
            @PathVariable("gameId") gameId: UUID?,
            @PathVariable("col") col: Int?,
            @PathVariable("row") row: Int?,
            model: Model): String {

        val gameController = gameState.getGameController(gameId)
                ?: return "redirect:/backend/index"

        val fieldController = gameController.getFieldController(col, row)
                ?: return "redirect:/backend/index"

        model.addAttribute("game", gameController.game)
        model.addAttribute("field", fieldController.field)
        gameController.sender.send(OverlayEvent(fieldController.field))
        return "backend/field"
    }

    @PostMapping("/game/{gameId}/field/{col}/{row}")
    fun postAnswer(
            @PathVariable("gameId") gameId: UUID?,
            @PathVariable("col") col: Int?,
            @PathVariable("row") row: Int?,
            @RequestParam("player") player: UUID?,
            @RequestParam("points") points: Int,
            @RequestParam("answer") answer: String): String {

        var pointsChange = points
        if (answer == "Wrong") {
            pointsChange *= -1
        }

        val gameController = gameState.getGameController(gameId)
                ?: return "redirect:/backend/index"

        var resolvedPlayer: Player? = null
        if (player != null) {
            val playerController = gameController.getPlayerController(player)
                    ?: return "redirect:/backend/index"

            playerController.updateScore(pointsChange)
            resolvedPlayer = playerController.player
        }

        val fieldController = gameController.getFieldController(col, row)
                ?: return "redirect:/backend/index"

        fieldController.markDone(player = resolvedPlayer)

        return if (answer == "wrong" && !fieldController.field.bonus) {
            "redirect:/backend/game/$gameId/field/$col/$row"
        } else {
            "redirect:/backend/game/$gameId"
        }
    }

    @PostMapping("game")
    fun createGame(): String {
        gameState.createGame()
        return "redirect:/backend"
    }
}
