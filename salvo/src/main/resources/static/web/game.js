
const urlParams = new URLSearchParams(window.location.search);
const myParam = urlParams.get('Gp');

fetch("http://localhost:8080/api/game_view/" + myParam)
     .then(function (response) {
          if (response.ok) {
               return response.json();
          }
     }).then(function (json) {
          app.game_view = json;
          app.colorearCasilla();
          app.playersName();
     })

var app = new Vue({
     el: "#app",
     data: {
          numeros: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
          letras: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
          game_view: {},
          players: {
               playerA: {
                    email: ""
               },
               playerB: {
                    email: ""
               }
          }
     },
     methods: {
          playersName: function () {
               app.players.playerA = app.game_view.gamePlayers.find(element => element.id == myParam).player;
               app.players.playerB = app.game_view.gamePlayers.find(element => element.id != myParam).player;
          },
          colorearCasilla: function () {

               function compare(a, b) {
                    if (a.id < b.id) {
                         return -1;
                    }
                    else if (a.id > b.id) {
                         return 1;
                    }
                    return 0;
               }

               var barco = app.game_view.ships.sort(compare);

               for (let j = 0; j < barco.length; j++) {
                    for (let i = 0; i < barco[j].locations.length; i++) {
                         document.getElementById(barco[j].locations[i]).className = "colorBarcos";
                    }
               }

          }
     }

});


