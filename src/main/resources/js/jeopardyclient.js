var stompClient;
var subscribedGame = -1;

const updateState = function (newState) {
    document.getElementById('message').innerText = newState
};

const handleGameAction = function() {

};

const subscribeToGame = function (game_id) {
    stompClient.subscribe('/topic/game/' + game_id, function () {
        handleGameAction();
    });

    document.getElementById('gamelist').style.visibility = "hidden";
    document.getElementById('playerlist').style.visibility = "visible";
    stompClient.send("/app/game/" + game_id + "/");
};

const handleGameAdded = function (game_info) {
    if (subscribedGame != -1)
    {
        return;
    }

    for (game of JSON.parse(game_info))
    {
        let node = document.createElement("li");
        node.innerText = game.id;
        node.onclick = function(e) {
            subscribeToGame.apply(null, [game.id]);
        };

        let gamelist = document.getElementById('gamelist');
        let duplicate = false;
        for (it of gamelist.children)
        {
            duplicate |= (it.innerText === game.id);
        }

        if (!duplicate)
        {
            gamelist.appendChild(node);
        }
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