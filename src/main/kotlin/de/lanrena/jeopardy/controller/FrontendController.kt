package de.lanrena.jeopardy.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@Suppress("unused")
open class FrontendController
{
    @RequestMapping("/")
    fun index() : String {
        return "frontend/index"
    }
}
