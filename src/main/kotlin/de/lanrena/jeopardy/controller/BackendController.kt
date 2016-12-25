package de.lanrena.jeopardy.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/backend")
@Suppress("unused")
open class BackendController(
        @Autowired val gameState: JeopardyController) {

    @GetMapping("")
    fun index(model: Model): String {
        model.addAttribute("games", gameState.listGames())
        return "backend/index"
    }

    @PostMapping(value = "game")
    fun create_game(): String {
        gameState.createGame()
        return "redirect:/backend"
    }
}
