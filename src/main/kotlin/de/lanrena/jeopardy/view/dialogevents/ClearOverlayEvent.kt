package de.lanrena.jeopardy.view.dialogevents

import de.lanrena.jeopardy.view.JsonMessage

class ClearOverlayEvent
    : JsonMessage(
        type = "OverlayEvent",
        payload = "")
