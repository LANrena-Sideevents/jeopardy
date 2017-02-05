let Jeopardy = {};

const handleGameAction = function(message, client) {
    switch (message['type'])
    {
        case "CombinedEvent":
            for (let event of message['payload'])
                handleGameAction(event, client);
            break;

        case "PlayerEvent":
            Jeopardy.SelectedGame().Players.push(
                new Jeopardy.Player(
                    message['payload'].id,
                    message['payload'].name,
                    message['payload'].color,
                    message['payload'].points));
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

    Jeopardy.Game = function (id) {
        this.id = ko.observable(id);
        this.Players = ko.observableArray();

        //noinspection JSUnusedGlobalSymbols
        this.selectGame = function() {
            Jeopardy.SelectedGame(this);
            window.stomp.unsubscribe('/topic/games');
            window.stomp.subscribe('/topic/game/' + this.id(), function (message) {
                handleGameAction(JSON.parse(message.body), window.stomp);
            });
        }
    };

    Jeopardy.Player = function(id, name, color, points) {
        this.id = ko.observable(id);
        this.name = ko.observable(name);
        this.color = ko.observable(color);
        this.points = ko.observable(points);
    };

    Jeopardy.Games = ko.observableArray();
    Jeopardy.SelectedGame = ko.observable();

    window.stomp.connect({}, function () {
        ko.applyBindings(Jeopardy);

        window.stomp.subscribe(TOPIC_PREFIX + '/games', function (game_result) {
            for (let game of JSON.parse(game_result.body)['payload']) {
                Jeopardy.Games.push(new Jeopardy.Game(game.id));
            }
        });

        Jeopardy.Message("Connected");
    });

    Jeopardy.Message("Contacting server...");
});
