let Jeopardy = {};

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
    let TOPIC_PREFIX = '/topic';

    window.stomp = Stomp.over(new SockJS('http://' + window.location.host + '/jeopardy'));
    window.stomp.clientId = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        let r = Math.random() * 16 | 0, v = c == 'x' ? r: (r & 0x3 | 0x8);
        return v.toString(16);
    });

    Jeopardy.Message = ko.observable();

    Jeopardy.Game = function (id, name, color) {
        let self = this;
        this.id = ko.observable(id);
        this.name = ko.observable(name);
        this.color = ko.observable(color);

        this.selectGame = function(){
        }
    };

    Jeopardy.Games = ko.observableArray();

    window.stomp.connect({}, function () {
        ko.applyBindings(Jeopardy);

        window.stomp.subscribe(TOPIC_PREFIX + '/games', function (game_result) {
            for (let game of JSON.parse(game_result.body)['payload']) {
                Jeopardy.Games.push(new Jeopardy.Game(game.id, null, null));
            }
        });

        Jeopardy.Message("Connected");
    });

    Jeopardy.Message("Contacting server...");
});
