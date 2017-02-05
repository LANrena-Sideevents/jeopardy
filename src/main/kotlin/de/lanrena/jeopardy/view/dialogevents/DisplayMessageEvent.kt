package de.lanrena.jeopardy.view.dialogevents

import de.lanrena.jeopardy.view.JsonMessage

class DisplayMessageEvent(message: String)
    : JsonMessage(
        type = "DisplayMessageEvent",
        payload = message)
