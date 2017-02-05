package de.lanrena.jeopardy.view.stickyevents

import de.lanrena.jeopardy.view.JsonMessage

class CombinedEvent(events: List<JsonMessage>)
    : JsonMessage(
        type = "CombinedEvent",
        payload = events)
