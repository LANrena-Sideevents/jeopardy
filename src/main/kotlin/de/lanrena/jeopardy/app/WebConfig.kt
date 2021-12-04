package de.lanrena.jeopardy.app

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
open class WebConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/js/")

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/css/")

        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/fonts/")

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }
}
