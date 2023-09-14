package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Field
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.FieldEvent

class FieldController(val field: Field,
                      private val sender: MessageSender) {
    suspend fun markDone(player: Player?) {
        field.player = player
        field.done = true
        sender.send(FieldEvent(field))
    }
}
