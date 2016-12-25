package de.lanrena.jeopardy.model

data class Field(
        val row : Int,
        val col : Int,
        val question : Question,
        val bonus : Boolean)
