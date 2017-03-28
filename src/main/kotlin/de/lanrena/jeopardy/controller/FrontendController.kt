package de.lanrena.jeopardy.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels
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

        val gameController = gameState.getGameController(gameId) ?: return response.sendRedirect("/frontend/index")
        val res: InputStream? = gameController.resolveResource(resId)

        Channels.newChannel(res).use({ inputChannel ->
            Channels.newChannel(response.outputStream).use({ outputChannel ->
                val buffer = ByteBuffer.allocateDirect(10240)
                var size: Long = 0

                while (inputChannel.read(buffer) != -1) {
                    buffer.flip()
                    size += outputChannel.write(buffer).toLong()
                    buffer.clear()
                }

                response.setContentLengthLong(size)
            })
        })
    }
}
