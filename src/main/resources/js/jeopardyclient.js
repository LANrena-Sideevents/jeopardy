const updateState = function (newState) {
    document.getElementById('message').innerText = newState
};

const handleGameAction = function(message, client) {
    switch (message['type'])
    {
        case "CombinedEvent":
            for (let event of message['payload'])
                handleGameAction(event, client);
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

        case "RemoveFieldEvent":
            let field = document.getElementById(message['payload']['field']);
            field.style.background = message['payload']['color'];
            field.innerText = '';
            break;
    }
};

document.addEventListener("DOMContentLoaded", function() {
    const stompClient = Stomp.over(new SockJS('http://[::1]:8080/jeopardy'));

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
                        handleGameAction(JSON.parse(message.body), stompClient);
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
