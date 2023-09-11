package de.lanrena.jeopardy.frontend

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.script
import kotlinx.html.styleLink

fun Application.configureFrontend() {
    routing {
        get("") {
            call.respondHtml {
                head {
                    script(type = "text/javascript", src = "/assets/webjars/sockjs-client/1.5.1/sockjs.min.js") {}
                    script(type = "text/javascript", src = "/assets/webjars/stomp-websocket/2.3.4/stomp.min.js") {}
                    script(type = "text/javascript", src = "/assets/webjars/knockout/3.5.1/knockout.js") {}
                    script(type = "text/javascript", src = "/js/jeopardyclient.js") {}
                    styleLink("/css/jeopardy.css")
                    styleLink("/css/8bit-style.css")
                }

                body {
                    div(id = "waitingroom") {

                    }
                }
            }

            <body>

            <div id="waitingroom" data-bind="ifnot: SelectedGame">
            <h2 id="message" data-bind="text: Message"></h2>
            <ul id="gamelist" data-bind="foreach: Games">
            <li data-bind="text: id, click: selectGame"></li>
            </ul>
            </div>

            <div id="overlay" data-bind="visible: Jeopardy.Overlay.shown">
            <div data-bind="text: Jeopardy.Overlay.text"/>
            <img data-bind="attr:{src: Jeopardy.Overlay.image}" max-width="100%" max-height="100%"/>
            <audio data-bind="audioBind: Jeopardy.Overlay.audio" loop="loop"/>
            <h1 data-bind="text: Jeopardy.Overlay.headline"/>
            </div>

            <table id="gameboard" data-bind="if: SelectedGame">
            <tr data-bind="with: SelectedGame().Categories">
            <th data-bind="text: col1"></th>
            <th data-bind="text: col2"></th>
            <th data-bind="text: col3"></th>
            <th data-bind="text: col4"></th>
            <th data-bind="text: col5"></th>
            <th id="scoreboard_header">Scoreboard</th>
            </tr>
            <tr>
            <td id="row1col1">100</td>
            <td id="row1col2">100</td>
            <td id="row1col3">100</td>
            <td id="row1col4">100</td>
            <td id="row1col5">100</td>
            <td id="scoreboard" rowspan="5">
            <ul id="players" data-bind="foreach: SelectedGame().Players">
            <li>
            <svg xmlns="http://www.w3.org/2000/svg"
            version="1.1"
            viewBox="0 0 150 150">
            <g transform="translate(0,-147)">
            <rect
            y="151.1012"
            x="51.404758"
            height="47.625"
            width="47.625"
            data-bind="style: { fill: color }"/>
            <rect
            y="198.72618"
            x="99.02977"
            height="47.625"
            width="47.625"
            data-bind="style: { fill: color }"/>
            <rect
            y="198.72618"
            x="3.7797601"
            height="47.625"
            width="47.625"
            data-bind="style: { fill: color }"/>
            <rect
            y="246.3512"
            x="51.404766"
            height="47.625"
            width="47.625"
            data-bind="style: { fill: color }"/>
            </g>
            </svg>
            <span data-bind="text: name"></span>
            <span data-bind="text: points" class="score"></span>
            </li>
            </ul>
            </td>
            </tr>
            <tr>
            <td id="row2col1">200</td>
            <td id="row2col2">200</td>
            <td id="row2col3">200</td>
            <td id="row2col4">200</td>
            <td id="row2col5">200</td>
            </tr>
            <tr>
            <td id="row3col1">300</td>
            <td id="row3col2">300</td>
            <td id="row3col3">300</td>
            <td id="row3col4">300</td>
            <td id="row3col5">300</td>
            </tr>
            <tr>
            <td id="row4col1">400</td>
            <td id="row4col2">400</td>
            <td id="row4col3">400</td>
            <td id="row4col4">400</td>
            <td id="row4col5">400</td>
            </tr>
            <tr>
            <td id="row5col1">500</td>
            <td id="row5col2">500</td>
            <td id="row5col3">500</td>
            <td id="row5col4">500</td>
            <td id="row5col5">500</td>
            </tr>
            </table>

            </body>
            </html>

        }
    }
}
