package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.PlayerEvent

class PlayerController(
        val player: Player,
        val game: Game,
        val sender: MessageSender) {

    suspend fun updatePlayer(name: String = player.name,
                     color: String = player.color,
                     points: Int = player.points) {

        player.name = name
        player.color = color
        player.points = points
        sender.send(PlayerEvent(player))
    }

    suspend fun updateScore(deltaPoints: Int) {
        updatePlayer(points = player.points + deltaPoints)
    }
}
