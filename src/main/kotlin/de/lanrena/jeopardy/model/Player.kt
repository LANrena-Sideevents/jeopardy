package de.lanrena.jeopardy.model

data class Color(
        val red : Byte,
        val blue : Byte,
        val green : Byte)
{
    override fun toString(): String {
        return "#ff" + red + blue + green
    }
}

data class Player(
        val name : String,
        var points : Int,
        val color : Color)