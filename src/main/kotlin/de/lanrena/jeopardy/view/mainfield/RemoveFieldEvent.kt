package de.lanrena.jeopardy.view.mainfield

import de.lanrena.jeopardy.model.Field
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.JsonMessage

class RemoveFieldEvent(field: Field, player: Player)
    : JsonMessage(
        type = "RemoveFieldEvent",
        payload = listOf(field, player.color))
