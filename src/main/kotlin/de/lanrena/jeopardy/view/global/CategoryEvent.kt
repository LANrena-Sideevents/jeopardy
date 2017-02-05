package de.lanrena.jeopardy.view.global

import de.lanrena.jeopardy.model.Category
import de.lanrena.jeopardy.view.JsonMessage

class CategoryEvent(category: Category)
    : JsonMessage(
        type = "CategoryEvent",
        payload = category)
