package de.lanrena.jeopardy.model

class GlobalGameState {
    internal val games: MutableList<Game> = mutableListOf()

    internal val screens: MutableList<Screen> = mutableListOf()

    fun addScreen(screen: Screen) {
        screens.add(screen)
    }
}
