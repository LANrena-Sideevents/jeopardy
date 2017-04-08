package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.view.dialogevents.ClearOverlayEvent
import de.lanrena.jeopardy.view.dialogevents.DisplayMessageEvent
import de.lanrena.jeopardy.view.dialogevents.OverlayEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Controller
@RequestMapping("/backend")
@Suppress("unused")
open class BackendController(
        @Autowired val template: SimpMessagingTemplate,
        @Autowired val gameState: JeopardyController) {

    @GetMapping("", "/index")
    fun index(model: Model): String {
        model.addAttribute("games", gameState.listGames())
        return "backend/index"
    }

    @GetMapping("/game")
    fun preventStackTrace(): String = "redirect:/backend/index"

    @GetMapping("/game/{gameId}")
    fun game(@PathVariable("gameId") gameId: UUID?, model: Model): String {
        val gameController = gameState.getGameController(gameId) ?: return "redirect:/backend/index"
        model.addAttribute("game", gameController.game)

        return when {
            gameController.game.dataLoaded -> {
                gameController.sender.send(ClearOverlayEvent())
                "backend/game"
            }
            else -> "backend/init_game"
        }
    }

    @PostMapping("/game/{gameId}/load")
    fun load_data(@PathVariable("gameId") gameId: UUID,
                  @RequestParam("game_data") gameData: MultipartFile): String {
        val gameController = gameState.getGameController(gameId) ?: return "redirect:/backend/index"
        gameController.loadGameData(gameData)
        return "redirect:/backend/game/$gameId"
    }

    @GetMapping("/game/{gameId}/player/{playerId}")
    fun edit_player_form(
            @PathVariable("gameId") gameId: UUID?,
            @PathVariable("playerId") playerId: UUID?,
            model: Model): String {

        val gameController = gameState.getGameController(gameId) ?: return "redirect:/backend/index"
        val playerController = gameController.getPlayerController(playerId) ?: return "redirect:/backend/game/$gameId"
        model.addAttribute("game", gameController.game)
        model.addAttribute("player", playerController.player)
        return "backend/editplayer"
    }

    @PostMapping("/game/{gameId}/player/{playerId}")
    fun edit_player_action(
            @PathVariable("gameId") gameId: UUID?,
            @PathVariable("playerId") playerId: UUID?,
            @RequestParam("player_name") player_name: String,
            @RequestParam("player_color") player_color: String,
            @RequestParam("player_points") player_points: String,
            model: Model): String {

        val gameController = gameState.getGameController(gameId) ?: return "redirect:/backend/index"
        val playerController = gameController.getPlayerController(playerId) ?: return "redirect:/backend/game/$gameId/player/$playerId"
        playerController.updatePlayer(player_name, player_color, player_points.toInt())
        model.addAttribute("game", gameController.game)
        return "redirect:/backend/game/$gameId/players"
    }

    @PostMapping("/game/{gameId}/players")
    fun add_player(@PathVariable("gameId") gameId: UUID,
                   @RequestParam("player_name") player_name: String): String {

        val gameController = gameState.getGameController(gameId) ?: return "redirect:/backend/index"
        gameController.addPlayer(player_name)
        return "redirect:/backend/game/$gameId/players"
    }

    @GetMapping("/game/{gameId}/players")
    fun list_players(@PathVariable("gameId") gameId: UUID?, model: Model): String {
        val gameController = gameState.getGameController(gameId) ?: return "redirect:/backend/index"
        model.addAttribute("game", gameController.game)
        return "backend/players"
    }

    @GetMapping("/game/{gameId}/field/{col}/{row}")
    fun display_field(
            @PathVariable("gameId") gameId: UUID?,
            @PathVariable("col") col: Int?,
            @PathVariable("row") row: Int?,
            model: Model): String {

        val gameController = gameState.getGameController(gameId) ?: return "redirect:/backend/index"
        val fieldController = gameController.getFieldController(col, row) ?: return "redirect:/backend/index"
        model.addAttribute("game", gameController.game)
        model.addAttribute("field", fieldController.field)
        gameController.sender.send(OverlayEvent(fieldController.field))
        return "backend/field"
    }

    @PostMapping("/game/{gameId}/field/{col}/{row}")
    fun post_answer(
            @PathVariable("gameId") gameId: UUID?,
            @PathVariable("col") col: Int?,
            @PathVariable("row") row: Int?,
            @RequestParam("player") player: UUID?,
            @RequestParam("points") points: Int,
            @RequestParam("answer") answer: String): String {

        val gameController = gameState.getGameController(gameId) ?: return "redirect:/backend/index"
        val fieldController = gameController.getFieldController(col, row) ?: return "redirect:/backend/index"
        fieldController.markDone()

        var pointsChange = points
        if (answer == "Wrong") {
            pointsChange *= -1
        }

        val playerController = gameController.getPlayerController(player) ?: return "redirect:/backend/game/$gameId"
        playerController.updateScore(points)

        if (answer == "wrong" && !fieldController.field.bonus) {
            return "redirect:/backend/game/$gameId/field/$col/$row"
        } else {
            return "redirect:/backend/game/$gameId"
        }
    }

    @PostMapping("game")
    fun create_game(): String {
        gameState.createGame()
        return "redirect:/backend"
    }

    @GetMapping("/message")
    fun send_message(): String {
        template.convertAndSend("/topic/messages", DisplayMessageEvent("asdf test 123"))
        return "redirect:/backend"
    }
}
