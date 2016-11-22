package de.lanrena.jeopardy.controller.backend

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import javax.servlet.http.HttpServletResponse

@Suppress("unused")
class GameCreationController {
    @RequestMapping("/game/new", method = arrayOf(RequestMethod.GET))
    internal fun createGameForm(): String {
        return "backend/new_game"
    }

    @RequestMapping("/game/new", method = arrayOf(RequestMethod.POST))
    internal fun createGameSubmit(response: HttpServletResponse) {
        response.sendRedirect("/backend/index")
    }
}
