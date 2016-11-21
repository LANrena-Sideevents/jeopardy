package de.lanrena.jeopardy.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class GameController {
    @RequestMapping("/", "/game")
    internal fun index(): String { return "index" }
}
