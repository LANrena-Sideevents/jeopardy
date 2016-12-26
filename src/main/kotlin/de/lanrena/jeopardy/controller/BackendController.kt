package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.view.global.DisplayMessageEvent
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

    @GetMapping("")
    fun index(model: Model): String {
        model.addAttribute("games", gameState.listGames())
        return "backend/index"
    }

    @GetMapping("/game/{gameid}")
    fun game(@PathVariable("gameid") gameId: UUID, model: Model): String {
        model.addAttribute("game", gameState.findGame(gameId))
        return "backend/game"
    }

    @PostMapping("/game/{gameid}/player")
    fun add_player(@PathVariable("gameid") gameId: UUID,
                   @RequestParam("player_name") player_name: String,
                   @RequestParam("player_color") player_color: String): String {
        gameState.addPlayer(gameId, player_name, player_color)
        return "redirect:/backend/game/" + gameId
    }

    @PostMapping(value = "game")
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
