package de.lanrena.jeopardy.model

data class Field(
        val row: Int,
        val col: Int,
        val question: String,
        val answer: String,
        val bonus: Boolean = false,
        var done: Boolean = false,
        var player: Player? = null)
