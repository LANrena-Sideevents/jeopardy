package de.lanrena.jeopardy.view.global

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.view.JsonMessage

class GameStartedEvent(game: Game)
    : JsonMessage(
        type = "GameStartedEvent",
        payload = game)
