var stompClient;

var updateState = function(newState) {
    document.getElementById('message').innerText = newState
};

document.addEventListener("DOMContentLoaded", function(event) {
    var socket = new SockJS('http://[::1]:8080/jeopardy');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {        
        stompClient.subscribe('/topic/games', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });

        stompClient.subscribe('/topic/messages', function (msg) {
            updateState(msg)
        });

        stompClient.send("test_message");
    });

    updateState("Contacting server...");
});

