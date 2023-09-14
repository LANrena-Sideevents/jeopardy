package de.lanrena.jeopardy.view

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RequestGameList")
data object RequestGameList : JsonMessage