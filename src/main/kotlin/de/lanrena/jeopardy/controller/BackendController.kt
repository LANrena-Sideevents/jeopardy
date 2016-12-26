package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.view.global.DisplayMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
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

    @PostMapping(value = "game")
    fun create_game(): String {
        gameState.createGame()
        return "redirect:/backend"
    }

    @GetMapping("/message")
    fun send_message(): String {
        template.convertAndSend("/topic/messages", DisplayMessage("asdf test 123"))
        return "redirect:/backend"
    }
}
