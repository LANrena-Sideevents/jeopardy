package de.lanrena.jeopardy

import de.lanrena.jeopardy.backend.backendModule
import de.lanrena.jeopardy.controller.JeopardyController
import de.lanrena.jeopardy.frontend.frontendModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import org.koin.core.logger.Level
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

internal fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger(level = Level.DEBUG)
        modules(
            module {
                single { this@configureDependencyInjection }
                single { log }
                single { environment.config }
            },

            module {
                singleOf(::JeopardyController)
            },

            backendModule(),
            frontendModule(),
        )
    }
}
