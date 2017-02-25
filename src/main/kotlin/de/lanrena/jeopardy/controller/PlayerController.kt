package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.stickyevents.PlayerEvent

class PlayerController(
        val player: Player,
        val game: Game,
        val sender: TopicSender) {

    fun updatePlayer(name: String, color: String, points: Int) {
        player.name = name
        player.color = color
        player.points = points
        sender.send(PlayerEvent(player))
    }
}
