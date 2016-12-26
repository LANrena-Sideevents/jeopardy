//noinspection ES6ConvertVarToLetConst
var stompClient;

const updateState = function (newState) {
    document.getElementById('message').innerText = newState
};

const handleGameAction = function() {

};

const subscribeToGame = function (game_id) {
    stompClient.unsubscribe('/topic/games');
    stompClient.subscribe('/topic/game/' + game_id, function () {
        handleGameAction();
    });

    document.getElementById('gamelist').style.visibility = "hidden";
    document.getElementById('playerlist').style.visibility = "visible";
};

const handleGameAdded = function (game_info) {
    for (let game of JSON.parse(game_info))
    {
        let node = document.createElement("li");
        node.innerText = game.id;
        node.onclick = function() {
            subscribeToGame.apply(null, [game.id]);
        };

        document.getElementById('gamelist').appendChild(node);
    }
};

document.addEventListener("DOMContentLoaded", function() {
    const socket = new SockJS('http://[::1]:8080/jeopardy');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/games', function (game_result) {
            handleGameAdded(JSON.parse(game_result.body).payload)
        });

        stompClient.subscribe('/topic/messages', function (msg) {
            updateState(JSON.parse(msg.body).payload)
        });

        updateState("Connected");
    });

    updateState("Contacting server...");
});
