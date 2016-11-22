package de.lanrena.jeopardy.controller.backend

import de.lanrena.jeopardy.model.GlobalGameState
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Suppress("unused")
@Controller
@RequestMapping("/backend")
class AdminController {
    @GetMapping("", "/games")
    internal fun index(gameState: GlobalGameState, redirect: RedirectAttributes): String {
        redirect.addFlashAttribute("screens", gameState.screens)
        return "backend/index"
    }
}