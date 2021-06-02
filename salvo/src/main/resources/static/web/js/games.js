
fetch("http://localhost:8080/api/games")
     .then(function (response) {
          if (response.ok) {
               return response.json();
          }
     }).then(function (json) {
          app.data = json;
          app.compareEmails();
          app.crearObject();
     })

var app = new Vue({
     el: "#app",
     data: {
          data: {},
          emailList: [],
          players: [],
          username: "",
          password: "",
          url: "/web/game.html?gp="
     },
     methods: {
          sumScores: function (email) {

               var scores = 0;

               for (let i = 0; i < app.data.games.length; i++) {
                    for (let j = 0; j < app.data.games[i].gamePlayers.length; j++) {
                         if (app.data.games[i].gamePlayers[j].player.email == email) {
                              scores += app.data.games[i].gamePlayers[j].score;
                         }

                    }
               }
               return scores;

          },
          totalWins: function (player) {

               var scoreWin = [];

               for (let i = 0; i < app.data.games.length; i++) {
                    for (let j = 0; j < app.data.games[i].gamePlayers.length; j++) {

                         if (app.data.games[i].gamePlayers[j].player.email == player) {
                              if (app.data.games[i].gamePlayers[j].score == 1) {
                                   scoreWin.push(app.data.games[i].gamePlayers[j].score);
                              }
                         }

                    }
               }
               return scoreWin.length;

          },
          totalLosses: function (player) {

               var losses = [];

               for (let i = 0; i < app.data.games.length; i++) {
                    for (let j = 0; j < app.data.games[i].gamePlayers.length; j++) {

                         if (app.data.games[i].gamePlayers[j].player.email == player) {
                              if (app.data.games[i].gamePlayers[j].score == 0) {
                                   losses.push(app.data.games[i].gamePlayers[j].score);
                              }
                         }

                    }
               }
               return losses.length;

          },
          totalTies: function (player) {

               var ties = [];

               for (let i = 0; i < app.data.games.length; i++) {
                    for (let j = 0; j < app.data.games[i].gamePlayers.length; j++) {

                         if (app.data.games[i].gamePlayers[j].player.email == player) {
                              if (app.data.games[i].gamePlayers[j].score == 0.5) {
                                   ties.push(app.data.games[i].gamePlayers[j].score);
                              }
                         }

                    }
               }
               return ties.length;
          },
          compareEmails: function () {
               for (let i = 0; i < app.data.games.length; i++) {
                    for (let m = 0; m < app.data.games[i].gamePlayers.length; m++) {
                         if (app.emailList.includes(app.data.games[i].gamePlayers[m].player.email) == false) {
                              app.emailList.push(app.data.games[i].gamePlayers[m].player.email);
                         }
                    }
               }
          },
          crearObject: function () {
               for (let i = 0; i < app.emailList.length; i++) {
                    app.players.push({
                         "email": app.emailList[i],
                         "totalScores": app.sumScores(app.emailList[i]),
                         "totalWins": app.totalWins(app.emailList[i]),
                         "totalLosses": app.totalLosses(app.emailList[i]),
                         "totalTies": app.totalTies(app.emailList[i])
                    });
               }
          },
          login: function () {
               $.post("/api/login", { username: app.username, password: app.password })
                    .done(function () {
                         location.reload()
                    })
                    .fail(function () {
                         document.getElementById("errorTexto").textContent = "If you do not have an account, register by pressing Sing In or check the password or username you entered";
                    })
          },
          createUser: function () {
               $.post("/api/players", { username: app.username, password: app.password }).done(
                    function () {
                         app.login();
                    }).fail(function () {
                         document.getElementById("errorTexto").textContent = "Error when trying to register, this account already exists";
                    })
          },
          logout: function () {
               $.post("/api/logout").done(function () { location.reload() })
          },
          createGame: function () {
               $.post("/api/games").done(function (data) {
                    location.href = "/web/game.html?gp=" + data.id;
               }).fail(function () {
                    alert("Upps! Algo fallo")
               })
          },
          joinGame: function (dataGame) {

               $.post("/api/games/"+ dataGame +"/players").done(function (data) {  
                    location.href = "/web/game.html?gp="+ data.id;
               }).fail(function () {
                    alert("Error:The game is full");
               })
          },
          mostrarForm: function () {
               document.getElementById("overlay").classList.add("active");
               document.getElementById("popUp").classList.add("active");
          },
          cerrarPopUp: function () {
               document.getElementById("overlay").classList.remove("active");
               document.getElementById("popUp").classList.remove("active");
          }

     }
})
