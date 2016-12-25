package de.lanrena.jeopardy.app

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@EnableWebMvc
@Configuration
open class WebConfig : WebMvcConfigurerAdapter() {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/js/")

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/css/")
    }
}
