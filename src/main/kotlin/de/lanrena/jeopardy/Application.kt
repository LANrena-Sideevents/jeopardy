package de.lanrena.jeopardy

import de.lanrena.jeopardy.backend.configureBackend
import de.lanrena.jeopardy.frontend.configureFrontend
import freemarker.cache.ClassTemplateLoader
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.EngineMain
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.routing.routing
import io.ktor.server.webjars.Webjars
import io.ktor.server.websocket.WebSockets
import io.ktor.util.converters.ConversionService
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.json.Json

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureDependencyInjection()
    install(Webjars)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }

    routing {
        configureBackend()
        configureFrontend()
    }
}
