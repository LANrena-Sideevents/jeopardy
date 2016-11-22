package de.lanrena.jeopardy.controller.frontend

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.lanrena.jeopardy.model.GlobalGameState
import de.lanrena.jeopardy.model.Screen
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
@Suppress("unused")
class GameController {
    @RequestMapping("/", "/game")
    internal fun index(): String {
        return "index"
    }

    @ResponseBody
    @JsonSerialize
    @RequestMapping("/create_screen")
    internal fun createScreen(request : HttpServletRequest, gameState: GlobalGameState) : Screen {
        val screen = Screen(UUID.randomUUID(), request.remoteAddr)
        gameState.addScreen(screen)
        return screen
    }
}
