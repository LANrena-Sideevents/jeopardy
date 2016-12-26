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
            let game_ids = JSON.parse(game_result.body)['payload'];
            for (let game_id of game_ids)
            {
                let node = document.createElement("li");
                node.innerText = game_id;
                node.onclick = function() {
                    stompClient.unsubscribe('/topic/games');
                    stompClient.subscribe('/topic/game/' + game_id, function (message) {
                        handleGameAction(JSON.parse(message));
                    });
                    document.getElementById('waitingroom').style.display = "none";
                    document.getElementById('gameboard').style.display = "table";
                };

                document.getElementById('gamelist').appendChild(node);
            }
        });

        stompClient.subscribe('/topic/messages', function (msg) {
            updateState(JSON.parse(msg.body)['payload'])
        });

        updateState("Connected");
    });

    updateState("Contacting server...");
});
