let Jeopardy = {};

document.addEventListener("DOMContentLoaded", function () {
    let TOPIC_PREFIX = '/topic';

    let appAdr = window.location.host || "[::1]:8080";
    window.stomp = Stomp.over(new SockJS('http://' + appAdr + '/jeopardy'));
    window.stomp.clientId = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        let r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });

    const handleGameAction = function (message, client) {
        switch (message['type']) {
            case "CombinedEvent":
                for (let event of message['payload'])
                    handleGameAction(event, client);
                break;

            case "PlayerEvent":
                Jeopardy.SelectedGame().addPlayer(
                    new Jeopardy.Player(
                        message['payload'].id,
                        message['payload'].name,
                        message['payload'].color,
                        message['payload'].points));
                break;

            case "CategoryEvent":
                let colProperty = 'col' + message['payload']['column'];
                Jeopardy.SelectedGame().Categories[colProperty](message['payload'].label);
                break;

            case "FieldEvent":
                let field = document.getElementById(message['payload']['field']);
                field.style.background = message['payload']['color'];
                field.innerText = '';
                break;

            case "OverlayEvent":
                let payload = message['payload'];
                if (payload.startsWith("image:")) {
                    Jeopardy.Overlay.image("/resource/" + Jeopardy.SelectedGame().id() + "/" + payload.substr(6));
                }
                if (payload.startsWith("audio:")) {
                    // https://commons.wikimedia.org/wiki/File:Speaker_Icon.svg
                    Jeopardy.Overlay.image("https://upload.wikimedia.org/wikipedia/commons/2/21/Speaker_Icon.svg");
                    Jeopardy.Overlay.audio("/resource/" + Jeopardy.SelectedGame().id() + "/" + payload.substr(6));
                } else {
                    Jeopardy.Overlay.text(payload)
                }
                break;

            case "ClearOverlayEvent":
                Jeopardy.Overlay.clear();
                break;
        }
    };

    const updateFunc = function (item) {
        if (this.id() !== item.id()) {
            return false;
        }

        for (let property of Object.keys(item)) {
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

            if (ko.isObservable(item[property])) {
                if (!this.hasOwnProperty(property)) {
                    this[property] = item[property];
                } else {
                    this[property](item[property]());
                }
            }
        }

        return true;
    };

    const addIfNotExists = function (list, item) {
        let wasUpdated = false;
        ko.utils.arrayForEach(list(), (listItem) => {
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

        this.Categories = {
            col1: ko.observable("Category 1"),
            col2: ko.observable("Category 2"),
            col3: ko.observable("Category 3"),
            col4: ko.observable("Category 4"),
            col5: ko.observable("Category 5")
        };

        //noinspection JSUnusedGlobalSymbols
        this.selectGame = function () {
            Jeopardy.SelectedGame(this);
            window.stomp.unsubscribe('/topic/games');
            window.stomp.subscribe('/topic/game/' + this.id(), function (message) {
                handleGameAction(JSON.parse(message.body), window.stomp);
            });
        };

        this.update = updateFunc;
        this.addPlayer = (player) => {
            addIfNotExists(this.Players, player)
        };
    };

    Jeopardy.Player = function (id, name, color, points) {
        this.id = ko.observable(id);
        this.name = ko.observable(name);
        this.color = ko.observable(color);
        this.points = ko.observable(points);

        this.update = updateFunc;
    };

    Jeopardy.Games = ko.observableArray();
    Jeopardy.SelectedGame = ko.observable();

    Jeopardy.Overlay = new function () {
        this.image = ko.observable();
        this.text = ko.observable();
        this.audio = ko.observable();

        this.shown = ko.pureComputed(function () {
            return this.image() !== undefined
                || this.text() !== undefined
                || this.audio() !== undefined;
        }, this);

        this.clear = function () {
            this.image(undefined);
            this.text(undefined);
            this.audio(undefined);
        }
    };

    ko.bindingHandlers.audioBind = {
        update: function (element, valueAccessor) {
            let value = ko.unwrap(valueAccessor());
            if (value === undefined) {
                element.pause();
                element.src = '';
            } else {
                element.src = value;
                element.play();
            }
        }
    };

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
