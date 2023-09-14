package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.view.JsonMessage

interface MessageSender {
    suspend fun send(message: JsonMessage)
}
