//noinspection ES6ConvertVarToLetConst
var stompClient;

const updateState = function (newState) {
    document.getElementById('message').innerText = newState
};

const handleGameAction = function(message) {
    switch (message['type'])
    {
        case "CombinedEvent":
            for (let event of message['payload'])
                handleGameAction(event);
            break;

        case "AddPlayerEvent":
            let player = message['payload'];
            let node = document.createElement("li");
            node.innerText = player['name'];

            let score = document.createElement("span");
            score.className += 'score';
            score.innerText = player['points'];
            node.appendChild(score);

            let players = document.getElementById('players');
            players.appendChild(node);

            break;
    }
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
                        handleGameAction(JSON.parse(message.body));
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
