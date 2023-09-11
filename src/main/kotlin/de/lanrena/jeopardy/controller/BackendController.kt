package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.OverlayEvent
import java.util.*


class BackendController() {

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
