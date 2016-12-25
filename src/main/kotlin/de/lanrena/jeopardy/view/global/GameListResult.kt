package de.lanrena.jeopardy.view.global

import com.google.gson.Gson
import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.view.JsonMessage

class GameListResult(vararg listGames: Game) : JsonMessage() {
    init {
        this.json = Gson().toJson(listGames.toList())
    }
}
