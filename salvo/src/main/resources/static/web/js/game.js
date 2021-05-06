
const urlParams = new URLSearchParams(window.location.search);
const getGamePlayer = urlParams.get('gp');

fetch("http://localhost:8080/api/game_view/" + getGamePlayer)
     .then(function (response) {
          if (response.ok) {
               return response.json();
          }
     }).then(function (json) {
          app.gameView = json;
          app.colorearCasilla();
          app.playersName();
          app.pintarSalvos();
          app.tiroAcertado();
     })

var app = new Vue({
     el: "#app",
     data: {
          clicks: 0,
          ship: "battleship",
          numeros: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
          letras: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
          gameView: {},
          players: {
               playerA: {},
               playerB: {}
          },
          ships: [
               {
                    "type": "battleship",
                    "locations": [],
                    "size": 4
               },
               {
                    "type": "carrier",
                    "locations": [],
                    "size": 4
               },
               {
                    "type": "destroyer",
                    "locations": [],
                    "size": 3
               },
               {
                    "type": "patrolBoat",
                    "locations": [],
                    "size": 2
               },
               {
                    "type": "submarine",
                    "locations": [],
                    "size": 3
               },
          ]

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
          colorearCasilla: function () {

               var barco = app.gameView.ships;

               for (let j = 0; j < barco.length; j++) {
                    for (let i = 0; i < barco[j].locations.length; i++) {
                         document.getElementById(barco[j].locations[i]).className = "colorBarcos";
                    }
               }

          },
          pintarSalvos: function () {
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
          tiroAcertado: function () {

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
               $.post({
                    url: "/api/games/players/" + getGamePlayer + "/ships",
                    data: JSON.stringify([
                         { "type": app.ships[0].type, "locations": app.ships[0].locations },
                         { "type": app.ships[1].type, "locations": app.ships[1].locations },
                         { "type": app.ships[2].type, "locations": app.ships[2].locations },
                         { "type": app.ships[3].type, "locations": app.ships[3].locations },
                         { "type": app.ships[4].type, "locations": app.ships[4].locations }
                    ]),
                    dataType: "text",
                    contentType: "application/json"
               })
                    .done(function (response, status, jqXHR) {
                         console.log("Ship added: " + response);
                         location.reload();
                    })
                    .fail(function (jqXHR, status, httpError) {
                         console.log("Failed to add ships: " + status + " " + httpError);
                    })
          },
          locationShip: function (letr, number) {

               this.clicks++;

               var shipSelect;

               app.ships.forEach(ship => {
                    if (ship.type == app.ship) {
                         shipSelect = ship;
                    }
               })

               var letra = app.letras.indexOf(letr);
               var numero = app.numeros.indexOf(number);
               var j = 0;

               shipSelect.locations.forEach(location => {

                    if (!sobreencimado(shipSelect)) {
                         if (document.getElementById("t"+location).className == "newColorShips") {
                              document.getElementById("t"+location).classList.remove("newColorShips");
                         }
                    }
               })

               if (this.clicks % 2 == 1) {
                    if (letra + shipSelect.size > 10) {
                         shipSelect.locations = [];

                         for (j; j < shipSelect.size; j++) {
                              shipSelect.locations.push(app.letras[letra - j] + app.numeros[numero]);
                         }
                    } else {
                         shipSelect.locations = [];
                         for (j; j < shipSelect.size; j++) {
                              shipSelect.locations.push(app.letras[letra + j] + app.numeros[numero]);
                         }
                    }

               } else {
                    if (numero + shipSelect.size > 10) {
                         shipSelect.locations = [];

                         for (j; j < shipSelect.size; j++) {
                              shipSelect.locations.push(app.letras[letra] + app.numeros[numero - j]);
                         }
                    } else {
                         shipSelect.locations = [];
                         for (j; j < shipSelect.size; j++) {
                              shipSelect.locations.push(app.letras[letra] + app.numeros[numero + j]);
                         }
                    }
               }

               if (!sobreencimado(shipSelect)) {
                    shipSelect.locations.forEach(location => {
                         document.getElementById("t"+location).className = "newColorShips";
                         document.getElementById("text").textContent = "" 
                    })
               }else{
                    document.getElementById("text").textContent = "no hay espacio para poner un barco" 
               }
          }
     }
});


function sobreencimado(ship) {

     var currentShip = app.ships.find(barco => barco.type == ship.type);
     var remainingShips = app.ships.filter(barco => barco.type != currentShip.type);

     for (let z = 0; z < remainingShips.length; z++) {
          for (let a = 0; a < remainingShips[z].locations.length; a++) {
               if (currentShip.locations.includes(remainingShips[z].locations[a])) {
                    return true;
               }
          }
     }

     return false;

}