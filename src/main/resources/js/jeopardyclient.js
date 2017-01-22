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
    let Jeopardy = {};
    let TOPIC_PREFIX = '/topic';

    window.stomp = Stomp.over(new SockJS('http://' + window.location.host + '/jeopardy'));
    window.stomp.clientId = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        let r = Math.random() * 16 | 0, v = c == 'x' ? r: (r & 0x3 | 0x8);
        return v.toString(16);
    });

    Jeopardy.Game = Backbone.Model.extend({
        defaults: {
            id: "",
            Name: "",
            Color: ""
        },
    });

    Jeopardy.Games = Backbone.Collection.extend({
        model: Jeopardy.Game,
    });

    let games = new Jeopardy.Games();
    window.games = games;

    Jeopardy.GameList = Backbone.View.extend({
        id: 'GameList',
        initialize: function(games) {
            _.bindAll(this, 'render', 'addGame');

            this.games = games;
            this.games.bind('reset', this.render);
            this.games.bind('add', this.addGame);
            this.render();
        },

        render: function() {
            const self = this;
            this.games.each(function(game) {
                self.addGame(game);
            });

            return this;
        },

        addGame: function(game) {
            const tdv = new Jeopardy.GameListItem(game);
            $(this.el).append(tdv.el);
        },
    });

    Jeopardy.GameListItem = Backbone.View.extend({
        className: 'game',
        events: {
            'click': 'selectGame',
        },
        initialize: function(model) {
            _.bindAll(this, 'selectGame');
            this.model = model;
            this.render();
        },

        render: function() {
            $(this.el).html('<li id="' + this.model.id + '">' + this.model.id + '</li>');
            $(this.el).attr('id', this.model.id);
            return this;
        },

        selectGame: function() {
            window.stomp.unsubscribe(TOPIC_PREFIX + '/games');
            window.stomp.subscribe(TOPIC_PREFIX + '/game/' + this.model.id, function (message) {
                //handleGameAction(JSON.parse(message.body), stompClient);
            });
            document.getElementById('waitingroom').style.display = "none";
            document.getElementById('gameboard').style.display = "table";
        },
    });

    window.stomp.connect({}, function () {
        Jeopardy.App = Backbone.Router.extend({
            routes: {
                '': 'index',
                '/': 'index',
                '#game': 'game',
            },

            index: function() {
                $('#gamelist').append(new Jeopardy.GameList(games).el);
            },
        });

        window.stomp.subscribe(TOPIC_PREFIX + '/games', function (game_result) {
            for (let game of JSON.parse(game_result.body)['payload']) {
                games.add(new Jeopardy.Game(game));
            }
        });

        updateState("Connected");

        window.app = new Jeopardy.App();
        Backbone.history.start();
    });

    updateState("Contacting server...");
});
