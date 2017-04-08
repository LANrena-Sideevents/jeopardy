package de.lanrena.jeopardy.view.stickyevents

import de.lanrena.jeopardy.model.Field
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

class FieldEvent(field: Field)
    : JsonMessage(type = "FieldEvent",
        payload = SimplifiedField(
                col = field.col,
                row = field.row,
                disabled = field.done,
                player = field.player))
