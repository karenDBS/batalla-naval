
const urlParams = new URLSearchParams(window.location.search);
const getGamePlayer = urlParams.get('gp');

var app = new Vue({
     el: "#app",
     data: {
          status: "",
          clicks: 0,
          detalle: "",
          ship: "Battleship",
          salvo: {
               "turnPlayer": 0,
               "locations": []
          },
          numeros: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
          letras: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
          gameView: {},
          players: {
               playerA: {},
               playerB: {}
          },
          ships: [
               {
                    "type": "Battleship",
                    "locations": [],
                    "size": 4
               },
               {
                    "type": "Carrier",
                    "locations": [],
                    "size": 5
               },
               {
                    "type": "Destroyer",
                    "locations": [],
                    "size": 3
               },
               {
                    "type": "PatrolBoat",
                    "locations": [],
                    "size": 2
               },
               {
                    "type": "Submarine",
                    "locations": [],
                    "size": 3
               },
          ],

     },
     methods: {
          playersName: function () {
               app.players.playerA = app.gameView.gamePlayers.find(element => element.id == getGamePlayer).player;
               if (app.gameView.gamePlayers.length == 1) {
                    app.players.playerB.email = "actualmente no hay un contrincante";
               } else {
                    app.players.playerB = app.gameView.gamePlayers.find(element => element.id != getGamePlayer).player;
               }
          },
          paintShips: function () {
               var barco = app.gameView.ships;

               for (let j = 0; j < barco.length; j++) {
                    for (let i = 0; i < barco[j].locations.length; i++) {
                         document.getElementById(barco[j].locations[i]).className = "colorShips";
                    }
               }

          },
          paintSalvos: function () {
               var salvoJugador = app.gameView.salvoes.filter(element => element.player == app.players.playerA.id);
               var oponente = app.gameView.salvoes.filter(element => element.player == app.players.playerB.id);

               for (let j = 0; j < salvoJugador.length; j++) {
                    for (let i = 0; i < salvoJugador[j].locations.length; i++) {
                         document.getElementById(salvoJugador[j].locations[i].toLowerCase()).className = "colorSalvoes";
                         document.getElementById(salvoJugador[j].locations[i].toLowerCase()).innerHTML = salvoJugador[j].turn;
                    }
               }

               for (let j = 0; j < oponente.length; j++) {
                    for (let i = 0; i < oponente[j].locations.length; i++) {
                         document.getElementById(oponente[j].locations[i]).className = "colorSalvoes";
                         document.getElementById(oponente[j].locations[i]).innerHTML = oponente[j].turn;
                    }
               }

          },
          successfulShot: function () {

               var oponente = app.gameView.salvoes.filter(element => element.player == app.players.playerB.id);

               for (let j = 0; j < oponente.length; j++) {
                    for (let i = 0; i < oponente[j].locations.length; i++) {

                         for (let k = 0; k < app.gameView.ships.length; k++) {
                              if (app.gameView.ships[k].locations.includes(oponente[j].locations[i])) {
                                   document.getElementById(oponente[j].locations[i]).className = "colorTiroAcertado";
                                   document.getElementById(oponente[j].locations[i]).innerHTML = oponente[j].turn;

                              }

                         }
                    }
               }
          },
          crearShips: function () {

               if (app.ships[0].locations.length != 0 && app.ships[1].locations.length != 0 && app.ships[2].locations.length != 0 && app.ships[3].locations.length != 0 && app.ships[4].locations.length != 0) {

                    $.post({
                         url: "/api/games/players/" + getGamePlayer + "/ships",
                         data: JSON.stringify(app.ships),
                         dataType: "text",
                         contentType: "application/json"
                    })
                         .done(function (response, status, jqXHR) {
                              console.log("Ship added: " + response);
                              location.reload();
                         })
                         .fail(function (jqXHR, status, httpError) {
                              console.log("Failed to add ships: " + jqXHR.status + " " + JSON.parse(jqXHR.responseText).Error);
                         })

               } else {
                    alert("You must locate all the ships to save them");
               }
          },
          locationShip: function (letr, number) {

               this.clicks++;

               var shipSelect = app.ships.find(ship => ship.type == app.ship);

               var letra = app.letras.indexOf(letr);
               var numero = app.numeros.indexOf(number);
               var j = 0;

               if (!app.overheated(shipSelect)) {
                    if (app.detalle == "vertical") {
                         shipSelect.locations.forEach((location, index) => {
                              if (document.getElementById('k' + location).className == shipSelect.type + "-" + index + "v") {
                                   document.getElementById('k' + location).classList.remove(shipSelect.type + "-" + index + "v");
                              }

                         })
                    } else if (app.detalle == "horizontal") {
                         shipSelect.locations.forEach((location, index) => {
                              if (document.getElementById('k' + location).className == shipSelect.type + "-" + index) {
                                   document.getElementById('k' + location).classList.remove(shipSelect.type + "-" + index);
                              }
                         })
                    }

                    if (this.clicks % 2 == 1) {
                         if (letra + shipSelect.size > 10) {
                              shipSelect.locations = [];

                              document.getElementById("text").textContent = "no hay espacio para mover el barco en esa direccion"
                         } else {
                              shipSelect.locations = [];
                              for (j; j < shipSelect.size; j++) {
                                   shipSelect.locations.push(app.letras[letra + j] + app.numeros[numero]);
                              }
                         }
                         app.detalle = "vertical";

                    } else {
                         if (numero + shipSelect.size > 10) {
                              shipSelect.locations = [];

                              document.getElementById("text").textContent = "no hay espacio para mover el barco en esa direccion"

                         } else {
                              shipSelect.locations = [];
                              for (j; j < shipSelect.size; j++) {
                                   shipSelect.locations.push(app.letras[letra] + app.numeros[numero + j]);
                              }
                         }
                         app.detalle = "horizontal";
                    }

                    if (!app.overheated(shipSelect)) {
                         if (app.detalle == "vertical") {
                              shipSelect.locations.forEach((location, index) => {
                                   document.getElementById('k' + location).className = shipSelect.type + "-" + index + "v";
                                   document.getElementById("text").textContent = ""
                              })

                         } else if (app.detalle == "horizontal") {
                              shipSelect.locations.forEach((location, index) => {
                                   document.getElementById('k' + location).className = shipSelect.type + "-" + index;
                                   document.getElementById("text").textContent = ""
                              })
                         }
                    } else {
                         document.getElementById("text").textContent = "there is no space to put the ship"
                    }

               }
          },
          overheated: function (ship) {

               var remainingShips = app.ships.filter(barco => barco.type != ship.type);

               for (let z = 0; z < remainingShips.length; z++) {
                    for (let a = 0; a < remainingShips[z].locations.length; a++) {
                         if (ship.locations.includes(remainingShips[z].locations[a])) {
                              return true;
                         }
                    }
               }

               return false;

          },
          salvosVerification: function (letr, number) {

               var salvoCurrent = app.gameView.salvoes.filter(element => element.player == app.players.playerA.id);

               for (let i = 0; i < salvoCurrent.length; i++) {
                    if (salvoCurrent[i].locations.includes(letr + number)) {
                         return true;
                    }
               }

               return false;
          },
          paintNewSalvos: function (letr, number) {

               if (app.gameView.salvoes.length == 0 || app.gameView.state == 'WAITING_YOU_SALVOS') {

                    var index = app.salvo.locations.indexOf(letr + number);

                    if (index == -1) {
                         if (app.salvo.locations.length < 5) {
                              if (app.gameView.salvoes != []) {

                                   if (!app.salvosVerification(letr, number)) {

                                        app.salvo.locations.push(letr + number);
                                        document.getElementById(letr.toLowerCase() + number).className = "newColorSalvos";

                                   } else {
                                        alert("you can't shoot in a place where it has already been shot")
                                   }
                              }
                         }
                    } else {
                         if (app.gameView.salvoes != []) {

                              if (!app.salvosVerification(letr, number)) {
                                   app.salvo.locations.splice(index, 1);
                                   document.getElementById(letr.toLowerCase() + number).classList.remove("newColorSalvos");

                              }
                         }
                    }
               }

          },
          saveSalvos: function () {
               if (app.salvo.locations.length == 5) {
                    $.post({
                         url: "/api/games/players/" + getGamePlayer + "/salvos",
                         data: JSON.stringify(app.salvo),
                         dataType: "text",
                         contentType: "application/json"
                    })
                         .done(function (response, status, jqXHR) {
                              console.log("Salvo added: " + response);
                              location.reload();
                         })
                         .fail(function (jqXHR, status, httpError) {
                              alert("Failed to add salvo: " + jqXHR.status + " " + JSON.parse(jqXHR.responseText).Error);
                              app.removeSalvos();
                         })

               } else {
                    alert("You must have 5 shots to save them");
               }
          },
          removeSalvos: function () {
               app.salvo.locations.forEach(location => {
                    document.getElementById(location.toLowerCase()).classList.remove("newColorSalvos");
               });
               app.salvo.locations = [];
          },
          increaseTurn: function () {
               var salvoCurrent = app.gameView.salvoes.filter(element => element.player == app.players.playerA.id);
               app.salvo.turnPlayer = salvoCurrent.length + 1;
          },
          paintOpponentShot: function () {

               app.gameView.hits.forEach(hit => {
                    hit.hits.forEach(location => {
                         document.getElementById(location.toLowerCase()).className = "colorTiroAcertado";
                         document.getElementById(location.toLowerCase()).innerHTML = hit.turn;
                    })
               })

          },
          sunks: function () {
               app.gameView.hits.forEach(hit => {
                    hit.sunks.forEach(sunk => {
                         sunk.locations.forEach(location => {
                              document.getElementById(location.toLowerCase()).className = "colorSunks";
                         })
                    })
               })
          },
          sunksOpponent: function () {
               if (app.gameView.state != 'WAITING_OPPONENT') {
                    app.gameView.opponentHits.forEach(hit => {
                         hit.sunks.forEach(sunk => {
                              sunk.locations.forEach(location => {
                                   document.getElementById(location).className = "colorSunks";
                              })
                         });

                    })
               }
          },
          volver: function () {
               location.href = "/web/games.html";
          },
          changes: function () {
               var status = {
                    WAITING_OPPONENT: "Esperando un oponente...",
                    WAITING_OPPONENT_SHIPS: "Esperando a que el oponente coloque sus barcos...",
                    WAITING_YOU_SALVOS: "Esperando a que dispares",
                    WAITING_OPPONENT_SALVOS: "Espera a que el oponente dispare...",
                    TIE: "Juego empatado!!",
                    LOSE: "Perdiste :'(",
                    WIN: "Felicidades ganaste!! :D"
               }

               app.status = status[app.gameView.state];

               if (app.gameView.state == 'TIE' || app.gameView.state == 'LOSE' || app.gameView.state == 'WIN') {
                    var title = document.getElementById("titulo");
                    title.textContent = "Game Over";
               }

          },
          reloadGame: function () {
               if (this.gameView.state.includes('OPPONENT')) {
                    setTimeout(this.data, 3000);
               }
          },
          data: function () {
               fetch("/api/game_view/" + getGamePlayer)
                    .then((response) => {
                         if (response.ok) {
                              return response.json();
                         }
                    }).then((json)=> {
                         this.gameView = json;
                         this.playersName();
                         this.paintShips();
                         this.paintSalvos();
                         this.successfulShot();
                         this.increaseTurn();
                         this.paintOpponentShot();
                         this.sunks();
                         this.sunksOpponent();
                         this.changes();
                         this.reloadGame();
                    })

          }
     },
     mounted: function () {
          this.data();
     }
});
