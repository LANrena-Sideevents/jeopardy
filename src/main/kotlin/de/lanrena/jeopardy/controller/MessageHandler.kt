package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.view.JsonMessage

interface MessageHandler {
    suspend fun handleMessage(message: JsonMessage): Boolean
}