package de.lanrena.jeopardy.controller

interface TopicSender {
    suspend fun send(message: Any)
}
