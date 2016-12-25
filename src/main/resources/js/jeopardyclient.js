var stompClient;

var updateState = function (newState) {
    document.getElementById('message').innerText = newState
};

var subscribeToGame = function (game_id) {

}

var handleGameAdded = function (game_info) {
    for (game of JSON.parse(game_info))
    {
        var node = document.createElement("li");
        node.innerText = game.id;
        document.getElementById('gamelist').appendChild(node);
    }
};

document.addEventListener("DOMContentLoaded", function(event) {
    var socket = new SockJS('http://[::1]:8080/jeopardy');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/games', function (game_result) {
            handleGameAdded(JSON.parse(game_result.body).payload)
        });

        stompClient.subscribe('/topic/messages', function (msg) {
            updateState(JSON.parse(msg.body).payload)
        });

        updateState("Connected");
        stompClient.send("/app/list_games");
    });

    updateState("Contacting server...");
});
