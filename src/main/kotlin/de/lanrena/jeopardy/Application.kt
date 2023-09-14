package de.lanrena.jeopardy

import de.lanrena.jeopardy.backend.configureBackend
import de.lanrena.jeopardy.frontend.configureFrontend
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.EngineMain
import io.ktor.server.routing.routing
import io.ktor.server.thymeleaf.Thymeleaf
import io.ktor.server.webjars.Webjars
import io.ktor.server.websocket.WebSockets
import kotlinx.serialization.json.Json
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureDependencyInjection()
    install(Webjars)
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }

    routing {
        configureBackend()
        configureFrontend()
    }
}
