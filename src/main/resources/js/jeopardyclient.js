//noinspection ES6ConvertVarToLetConst
var stompClient;

const updateState = function (newState) {
    document.getElementById('message').innerText = newState
};

const handleGameAction = function() {
    // switch ()
};

document.addEventListener("DOMContentLoaded", function() {
    const socket = new SockJS('http://[::1]:8080/jeopardy');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/games', function (game_result) {
            let game_info = JSON.parse(game_result.body)['payload'];
            for (let game of game_info)
            {
                let node = document.createElement("li");
                node.innerText = game.id;
                node.onclick = function() {
                    stompClient.unsubscribe('/topic/games');
                    stompClient.subscribe('/topic/game/' + game.id, function (message) {
                        handleGameAction(JSON.parse(message));
                    });
                    document.getElementById('waitingroom').style.display = "none";
                    document.getElementById('gameboard').style.display = "table";
                };

                document.getElementById('gamelist').appendChild(node);
            }
        });

        stompClient.subscribe('/topic/messages', function (msg) {
            updateState(JSON.parse(msg.body).payload)
        });

        updateState("Connected");
    });

    updateState("Contacting server...");
});
