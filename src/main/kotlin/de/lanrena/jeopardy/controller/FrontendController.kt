package de.lanrena.jeopardy.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.io.InputStream
import java.util.*
import javax.servlet.http.HttpServletResponse

@Controller
@Suppress("unused")
open class FrontendController(
        @Autowired val gameState: JeopardyController) {

    @RequestMapping(path = arrayOf("/", "frontend/index"))
    fun index(): String {
        return "frontend/index"
    }

    @RequestMapping("/resource/{gameId}/{resId}")
    fun serveResource(response: HttpServletResponse,
                      @PathVariable("gameId") gameId: UUID?,
                      @PathVariable("resId") resId: String) {

        val gameController = gameState.getGameController(gameId)
                ?: return response.sendError(404)

        val res: InputStream = gameController.resolveResource(resId)
                ?: return response.sendError(404)

        val size = StreamUtils.copy(res, response.outputStream).toLong()
        response.setContentLengthLong(size)
    }
}
