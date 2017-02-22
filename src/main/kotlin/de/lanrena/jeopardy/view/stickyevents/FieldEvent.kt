package de.lanrena.jeopardy.view.stickyevents

import de.lanrena.jeopardy.model.FieldState
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.JsonMessage

class SimplifiedField(val col: Int,
                      val row: Int,
                      val disabled: Boolean,
                      val player: Player? = null) {

    override fun toString(): String {
        return "{'col':'" + this.col + "'" +
                "'row':'" + this.row + "'" +
                "'disabled':'" + this.disabled + "'}"
    }
}

class FieldEvent(field: FieldState)
    : JsonMessage(type = "FieldEvent",
        payload = SimplifiedField(
                col = field.field.col,
                row = field.field.row,
                disabled = field.player != null,
                player = field.player))
