package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.view.dialogevents.DisplayMessageEvent
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

    @GetMapping("/game/{gameid}")
    fun game(@PathVariable("gameid") gameId: UUID?, model: Model): String {
        val gameController = gameState.getGameControlle(gameId) ?: return "redirect:/backend/index"
        model.addAttribute("game", gameController.game)
        return if (gameController.game.dataLoaded) "backend/game" else "backend/init_game"
    }

    @PostMapping("/game/{gameid}/load")
    fun load_data(@PathVariable("gameid") gameId: UUID,
                  @RequestParam("game_data") gameData: MultipartFile): String {
        val gameController = gameState.getGameControlle(gameId) ?: return "redirect:/backend/index"
        gameController.loadGameData(gameData)
        return "redirect:/backend/game/$gameId"
    }

    @GetMapping("/game/{gameid}/player/{playerid}")
    fun edit_player_form(
            @PathVariable("gameid") gameId: UUID?,
            @PathVariable("playerid") playerId: UUID?,
            model: Model): String {

        val gameController = gameState.getGameControlle(gameId) ?: return "redirect:/backend/index"
        val playerController = gameController.getPlayerController(playerId) ?: return "redirect:/backend/game/$gameId"
        model.addAttribute("game", gameController.game)
        model.addAttribute("player", playerController.player)
        return "backend/editplayer"
    }

    @PostMapping("/game/{gameid}/player/{playerid}")
    fun edit_player_action(
            @PathVariable("gameid") gameId: UUID?,
            @PathVariable("playerid") playerId: UUID?,
            @RequestParam("player_name") player_name: String,
            @RequestParam("player_color") player_color: String,
            @RequestParam("player_points") player_points: String,
            model: Model): String {

        val gameController = gameState.getGameControlle(gameId) ?: return "redirect:/backend/index"
        val playerController = gameController.getPlayerController(playerId) ?: return "redirect:/backend/game/$gameId/player/$playerId"
        playerController.updatePlayer(player_name, player_color, player_points.toInt())
        model.addAttribute("game", gameController.game)
        return "redirect:/backend/game/$gameId/players"
    }

    @PostMapping("/game/{gameid}/players")
    fun add_player(@PathVariable("gameid") gameId: UUID,
                   @RequestParam("player_name") player_name: String): String {

        val gameController = gameState.getGameControlle(gameId) ?: return "redirect:/backend/index"
        gameController.addPlayer(player_name)
        return "redirect:/backend/game/$gameId/players"
    }

    @GetMapping("/game/{gameid}/players")
    fun list_players(@PathVariable("gameid") gameId: UUID?, model: Model): String {
        val gameController = gameState.getGameControlle(gameId) ?: return "redirect:/backend/index"
        model.addAttribute("game", gameController.game)
        return "backend/players"
    }

    @GetMapping("/game/{gameid}/field/{col}/{row}")
    fun display_field(
            @PathVariable("gameid") gameId: UUID?,
            @PathVariable("col") col: Int?,
            @PathVariable("row") row: Int?,
            model: Model): String {

        val gameController = gameState.getGameControlle(gameId) ?: return "redirect:/backend/index"
        val fieldController = gameController.getFieldController(col, row) ?: return "redirect:/backend/index"
        model.addAttribute("game", gameController.game)
        model.addAttribute("field", fieldController.field)
        return "backend/field"
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
