package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.model.Game
import de.lanrena.jeopardy.model.Player
import de.lanrena.jeopardy.view.stickyevents.PlayerEvent
import org.springframework.messaging.simp.SimpMessagingTemplate

class PlayerController(
        val player: Player,
        val game: Game,
        val template: SimpMessagingTemplate?) {

    fun updatePlayer(name: String, color: String, points: Int) {
        player.name = name
        player.color = color
        player.points = points

        template?.convertAndSend("/topic/game/" + game.id, PlayerEvent(player))
    }
}
