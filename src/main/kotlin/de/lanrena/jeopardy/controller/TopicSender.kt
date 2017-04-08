package de.lanrena.jeopardy.controller

interface TopicSender {
    fun send(message: Any): Unit?
}
