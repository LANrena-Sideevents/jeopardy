package de.lanrena.jeopardy.view.dialogevents

import de.lanrena.jeopardy.model.Field
import de.lanrena.jeopardy.view.JsonMessage

class OverlayEvent(field: Field)
    : JsonMessage(
        type = "OverlayEvent",
        payload = field.question)