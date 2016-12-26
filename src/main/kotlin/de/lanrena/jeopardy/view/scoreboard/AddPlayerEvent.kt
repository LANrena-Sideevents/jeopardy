package de.lanrena.jeopardy.view.scoreboard

import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.JsonMessage

class AddPlayerEvent(player: Player)
    : JsonMessage(
        type = "AddPlayerEvent",
        payload = player)