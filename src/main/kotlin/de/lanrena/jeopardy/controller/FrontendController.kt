package de.lanrena.jeopardy.controller

import java.io.InputStream
import java.util.*

@Suppress("unused")
open class FrontendController(
        private val gameState: JeopardyController) {

    @RequestMapping(path = ["/", "frontend/index"])
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
