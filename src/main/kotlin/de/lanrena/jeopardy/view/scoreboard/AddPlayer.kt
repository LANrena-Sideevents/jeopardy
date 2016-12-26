package de.lanrena.jeopardy.view.scoreboard

import com.google.gson.Gson
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.JsonMessage

class AddPlayer(player: Player) : JsonMessage()
{
    init {
        this.json = Gson().toJson(player)
    }
}
