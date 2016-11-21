package de.lanrena.jeopardy.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class AdminController {
    @RequestMapping("/backend")
    internal fun index(): String { return "backend/index" }
}