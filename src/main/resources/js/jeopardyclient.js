let Jeopardy = {};

document.addEventListener("DOMContentLoaded", function() {
    let TOPIC_PREFIX = '/topic';

    let appAddr = window.location.host || "[::1]:8080";
    window.stomp = Stomp.over(new SockJS('http://' + appAddr + '/jeopardy'));
    window.stomp.clientId = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        let r = Math.random() * 16 | 0, v = c == 'x' ? r: (r & 0x3 | 0x8);
        return v.toString(16);
    });

    const handleGameAction = function(message, client) {
        switch (message['type'])
        {
            case "CombinedEvent":
                for (let event of message['payload'])
                    handleGameAction(event, client);
                break;

            case "PlayerEvent":
                let player = new Jeopardy.Player(
                    message['payload'].id,
                    message['payload'].name,
                    message['payload'].color,
                    message['payload'].points);
                addIfNotExists(Jeopardy.SelectedGame().Players, player);
                break;

            case "RemoveFieldEvent":
                let field = document.getElementById(message['payload']['field']);
                field.style.background = message['payload']['color'];
                field.innerText = '';
                break;
        }
    };

    const updateFunc = function (item) {
        if (this.id != item.id) {
            return false;
        }

        for (let property of item) {
            if (!item.hasOwnProperty(property)) {
                continue;
            }

            if (item[property] instanceof ko.observableArray) {
                this[property] = this[property] || ko.observableArray();
                ko.utils.arrayForEach(item[property], (otherItem) => {
                    addIfNotExists(this[property], otherItem);
                });
                continue;
            }

            if (item[property] instanceof ko.observable) {
                if (!this.hasOwnProperty(property)) {
                    this[property] = item[property];
                } else {
                    this[property](item[property]);
                }
            }
        }

        return true;
    };

    const addIfNotExists = function (list, item) {
        let wasUpdated = false;
        ko.utils.arrayForEach(list, (listItem) => {
            wasUpdated |= listItem.update(item);
        });

        if (!wasUpdated) {
            list.push(item);
        }
    };

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
        };

        this.update = updateFunc;
    };

    Jeopardy.Player = function(id, name, color, points) {
        this.id = ko.observable(id);
        this.name = ko.observable(name);
        this.color = ko.observable(color);
        this.points = ko.observable(points);

        this.update = updateFunc;
    };

    Jeopardy.Games = ko.observableArray();
    Jeopardy.SelectedGame = ko.observable();

    window.stomp.connect({}, function () {
        ko.applyBindings(Jeopardy);

        window.stomp.subscribe(TOPIC_PREFIX + '/games', function (game_result) {
            for (let game of JSON.parse(game_result.body)['payload']) {
                let item = new Jeopardy.Game(game.id);
                addIfNotExists(Jeopardy.Games, item);
            }
        });

        Jeopardy.Message("Connected");
    });

    Jeopardy.Message("Contacting server...");
});
