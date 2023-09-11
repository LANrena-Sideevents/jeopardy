package de.lanrena.jeopardy

import de.lanrena.jeopardy.backend.configureBackend
import de.lanrena.jeopardy.frontend.configureFrontend
import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.EngineMain
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.webjars.Webjars

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureDependencyInjection()
    install(Webjars)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    configureBackend()
    configureFrontend()
}
