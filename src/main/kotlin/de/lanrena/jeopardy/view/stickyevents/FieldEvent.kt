package de.lanrena.jeopardy.view.stickyevents

import de.lanrena.jeopardy.model.Field
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.JsonMessage

class FieldEvent(field: Field, player: Player)
    : JsonMessage(
        type = "FieldEvent",
        payload = listOf(field, player.color))
