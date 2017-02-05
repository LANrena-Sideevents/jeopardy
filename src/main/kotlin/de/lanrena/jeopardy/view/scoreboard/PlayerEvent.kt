package de.lanrena.jeopardy.view.scoreboard

import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.JsonMessage

class PlayerEvent(player: Player)
    : JsonMessage(
        type = "PlayerEvent",
        payload = player)