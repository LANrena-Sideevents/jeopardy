package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.view.dialogevents.DisplayMessageEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
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
        val findGame = gameState.findGame(gameId!!) ?: return "redirect:/backend/index"
        model.addAttribute("game", findGame)
        return "backend/game"
    }

    @GetMapping("/game/{gameid}/player/{playerid}")
    fun edit_player_form(
            @PathVariable("gameid") gameId: UUID?,
            @PathVariable("playerid") playerId: UUID?,
            model: Model): String {

        val game = gameState.findGame(gameId!!) ?: return "redirect:/backend/index"
        model.addAttribute("game", game)

        val player = gameState.findPlayer(game, playerId!!) ?: return "redirect:/backend/game/$gameId"
        model.addAttribute("player", player)

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

        val game = gameState.findGame(gameId!!) ?: return "redirect:/backend/index"
        gameState.updatePlayer(game, playerId!!, player_name, player_color, player_points.toInt())

        model.addAttribute("game", game)
        return "redirect:/backend/game/$gameId/players"
    }

    @PostMapping("/game/{gameid}/players")
    fun add_player(@PathVariable("gameid") gameId: UUID,
                   @RequestParam("player_name") player_name: String,
                   @RequestParam("player_color") player_color: String): String {
        gameState.addPlayer(gameId, player_name, player_color)
        return "redirect:/backend/game/$gameId/players"
    }

    @GetMapping("/game/{gameid}/players")
    fun list_players(@PathVariable("gameid") gameId: UUID?, model: Model): String {
        val findGame = gameState.findGame(gameId!!) ?: return "redirect:/backend/index"
        model.addAttribute("game", findGame)
        return "backend/players"
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
