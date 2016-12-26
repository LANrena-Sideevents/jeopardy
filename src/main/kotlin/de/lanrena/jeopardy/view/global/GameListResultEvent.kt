package de.lanrena.jeopardy.view.global

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.view.JsonMessage

class GameListResultEvent(vararg listGames: Game)
    : JsonMessage(
        type = "GameListResultEvent",
        payload = listGames.toList())