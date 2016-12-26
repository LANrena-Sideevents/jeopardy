package de.lanrena.jeopardy.view.scoreboard

import de.lanrena.jeopardy.view.JsonMessage

class UpdatePlayerEvent()
    : JsonMessage(
        type = "UpdatePlayerEvent",
        payload = "")
