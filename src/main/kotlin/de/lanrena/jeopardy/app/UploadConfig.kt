package de.lanrena.jeopardy.app

import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties
import org.springframework.context.annotation.Configuration
import javax.servlet.MultipartConfigElement

@Configuration
open class UploadConfig(multipartProperties: MultipartProperties) : MultipartAutoConfiguration(multipartProperties) {
    val maxSize: Long = 25 * 1024 * 1024

    override fun multipartConfigElement(): MultipartConfigElement {
        val oldConfig = super.multipartConfigElement()
        return MultipartConfigElement(
                oldConfig.location,
                maxSize,
                maxSize,
                oldConfig.fileSizeThreshold)
    }
}