let Jeopardy = {};

document.addEventListener("DOMContentLoaded", function () {
    const handleGameAction = function (message) {
        switch (message['type']) {
            case "CombinedEvent":
                for (let event of message['payload'])
                    handleGameAction(event);
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
                let field = document.getElementById('row' + message['payload']['row'] + 'col' + message['payload']['col']);
                if (message['payload']['disabled'])
                    field.className += "disabled";
                break;

            case "OverlayEvent":
                let payload = message['payload'];
                if (payload.startsWith("image:")) {
                    Jeopardy.Overlay.image("/resource/" + Jeopardy.SelectedGame().id() + "/" + payload.substr(6));
                } else if (payload.startsWith("audio:")) {
                    // https://commons.wikimedia.org/wiki/File:Speaker_Icon.svg
                    Jeopardy.Overlay.image("https://upload.wikimedia.org/wikipedia/commons/2/21/Speaker_Icon.svg");
                    Jeopardy.Overlay.audio("/resource/" + Jeopardy.SelectedGame().id() + "/" + payload.substr(6));
                } else if (payload.startsWith("big:")) {
                    Jeopardy.Overlay.headline(payload.substr(4))
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
            //window.stomp.unsubscribe('/topic/games');
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
        this.headline = ko.observable();

        //noinspection JSUnusedGlobalSymbols
        this.shown = ko.pureComputed(function () {
            return this.image() !== undefined
                || this.text() !== undefined
                || this.audio() !== undefined
                || this.headline() !== undefined;
        }, this);

        this.clear = function () {
            this.image(undefined);
            this.text(undefined);
            this.audio(undefined);
            this.headline(undefined);
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


    let appAdr = "ws://" + (window.location.host || "[::1]:8080") + "/jeopardy";
    const socket = new WebSocket(appAdr);

    socket.addEventListener("message", (event) => {
        console.log("Message from server ", event.data);

        if (event.data['type'] === "GameEvent") {
            for (let game of JSON.parse(event.data)['payload']) {
                let item = new Jeopardy.Game(game.id);
                addIfNotExists(Jeopardy.Games, item);
            }
        } else {
            handleGameAction(JSON.parse(event.data));
        }
    });

    socket.addEventListener("open", () => {
        ko.applyBindings(Jeopardy);

        socket.send({
            type: "RequestGameList"
        });

        Jeopardy.Message("Connected");
    });

    Jeopardy.Message("Contacting server...");
});
