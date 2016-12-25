package de.lanrena.jeopardy.app

import de.lanrena.jeopardy.controller.JeopardyController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@Suppress("unused")
open class JeopardyBeanConfig {
    open val controller: JeopardyController
        @Bean()
        get() = JeopardyController()
}
