package de.lanrena.jeopardy.view.global

import com.google.gson.Gson
import de.lanrena.jeopardy.view.JsonMessage

class DisplayMessage(message: String) : JsonMessage()
{
    init {
        this.json = Gson().toJson(message)
    }
}
