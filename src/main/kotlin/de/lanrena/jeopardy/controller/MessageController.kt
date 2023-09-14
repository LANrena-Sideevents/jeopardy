package de.lanrena.jeopardy.controller

import de.lanrena.jeopardy.io.WebSocketConnectionManager
import de.lanrena.jeopardy.view.GameEvent
import de.lanrena.jeopardy.view.JsonMessage
import de.lanrena.jeopardy.view.RequestGameList
import de.lanrena.jeopardy.view.RequestGameState

class MessageController(
    private val jeopardyController: JeopardyController,
    private val webSocketConnectionManager: WebSocketConnectionManager
) {

    init {
        webSocketConnectionManager.addHandler(object : MessageHandler {
            override suspend fun handleMessage(message: JsonMessage): Boolean {
                if (message is RequestGameList) {
                    webSocketConnectionManager.send(GameEvent(*jeopardyController.listGames().toTypedArray()))
                    return true
                }
                return false
            }
        })

        webSocketConnectionManager.addHandler(object : MessageHandler {
            override suspend fun handleMessage(message: JsonMessage): Boolean {
                if (message is RequestGameState) {
                    val response = jeopardyController.getGameController(message.gameId)?.getCombinedState()
                        ?: return false
                    webSocketConnectionManager.send(response)
                    return true
                }
                return false
            }
        })
    }
}
