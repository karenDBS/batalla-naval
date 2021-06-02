package com.codemasters.salvo.controllers;

import com.codemasters.salvo.models.*;
import com.codemasters.salvo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* REQUESTMAPPING DE GAMES: El primero es para enviar informacion de un nuevo juego y el segundo para
      mostrar el JSON del usuario autenticado. */

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> newGame(Authentication authentication) {

        if (!isGuest(authentication)) {

            LocalDateTime date = LocalDateTime.now();

            Game game = new Game(date);
            Player authPlayer = playerRepository.findByUserName(authentication.getName());
            GamePlayer gamePlayer = new GamePlayer(date, game, authPlayer);

            gameRepository.save(game);
            gamePlayerRepository.save(gamePlayer);

            return new ResponseEntity<>(makeMap("id", gamePlayer.getId()), HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>(makeMap("Error", "you are not authenticated"), HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(path = "/games/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long gameId, Authentication authentication) {

        if (!isGuest(authentication)) {

            Optional<Game> game = gameRepository.findById(gameId);
            Player playerAuth = playerRepository.findByUserName(authentication.getName());

            if (game.isPresent()) {

                Game getGame = game.get();

                if (getGame.getGamePlayers().size() < 2) {


                    if (game.get().getGamePlayers().stream().anyMatch(gameplayer -> gameplayer.getPlayer().getId() != playerAuth.getId())) {

                        GamePlayer gamePlayer = new GamePlayer(LocalDateTime.now(), game.get(), playerAuth);
                        gamePlayerRepository.save(gamePlayer);
                        return new ResponseEntity<>(makeMap("id", gamePlayer.getId()), HttpStatus.CREATED);

                    } else {
                        return new ResponseEntity<>(makeMap("Error", "You cannot join a player who is already in this game"), HttpStatus.FORBIDDEN);
                    }

                } else {
                    return new ResponseEntity<>(makeMap("Error", "This game is full"), HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(makeMap("Error", "This game doesn't exist"), HttpStatus.NOT_FOUND);
            }

        } else {
            return new ResponseEntity<>(makeMap("Error", "you are not authenticated"), HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping("/games")
    public Map<String, Object> getAll(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if (!isGuest(authentication)) {
            Player authPlayer = playerRepository.findByUserName(authentication.getName());
            dto.put("player", makePlayerDTO(authPlayer));
        } else {
            dto.put("player", null);
        }

        dto.put("games", gameRepository.findAll().stream().map(this::makeGameDTO).collect(toList()));
        return dto;

    }

    /* REQUESTMAPPING DE PLAYERS: El primero para enviar la informacion de un jugador dependiendo de si esta logueado,
      si puso correctamente sus datos o si el nombre que puso no existe ya. Y el segundo para mostrar el JSON de ese player. */

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String username, @RequestParam String password) {

        if (username.isEmpty() || password.isEmpty())
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        if (playerRepository.findByUserName(username) != null)
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        playerRepository.save(new Player(username, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/players")
    public List<Map<String, Object>> players() {
        return playerRepository.findAll().stream().map(this::makePlayerDTO).collect(toList());
    }

    /*NAVES DE UN JUGADOR: */

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> saveShips(@PathVariable long gamePlayerId, @RequestBody Set<Ship> ships, Authentication authentication) {

        if (isGuest(authentication))
            return new ResponseEntity<>(makeMap("Error", "you are not authenticated"), HttpStatus.FORBIDDEN);

        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);

        if (gamePlayer.isEmpty())
            return new ResponseEntity<>(makeMap("Error", "there is no player"), HttpStatus.FORBIDDEN);

        Player playerAuth = playerRepository.findByUserName(authentication.getName());
        GamePlayer getGamePlayer = gamePlayer.get();

        if (playerAuth.getId() != getGamePlayer.getPlayer().getId())
            return new ResponseEntity<>(makeMap("Error", "you can't see this resource"), HttpStatus.FORBIDDEN);

        if (!getGamePlayer.getShips().isEmpty())
            return new ResponseEntity<>(makeMap("Error", "you already have boats"), HttpStatus.FORBIDDEN);

        if (ships.size() < 5)
            return new ResponseEntity<>(makeMap("Error", "you don't have enough boats"), HttpStatus.FORBIDDEN);
        if (ships.size() > 6)
            return new ResponseEntity<>(makeMap("Error", "You can not put more boats"), HttpStatus.FORBIDDEN);

        for (Ship ship : ships) {
            shipRepository.save(new Ship(ship.getType(), getGamePlayer, ship.getLocations()));
        }

        return new ResponseEntity<>(makeMap("Yes", "Created"), HttpStatus.CREATED);

    }


    /*SALVOS DE UN JUGADOR: */

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> saveSalvoes(@PathVariable long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication) {

        if (isGuest(authentication))
            return new ResponseEntity<>(makeMap("Error", "you are not authenticated"), HttpStatus.FORBIDDEN);

        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);

        if (gamePlayer.isEmpty())
            return new ResponseEntity<>(makeMap("Error", "there is no player"), HttpStatus.FORBIDDEN);

        Player playerAuth = playerRepository.findByUserName(authentication.getName());
        GamePlayer getGamePlayer = gamePlayer.get();

        if (playerAuth.getId() != getGamePlayer.getPlayer().getId())
            return new ResponseEntity<>(makeMap("Error", "you can't see this resource"), HttpStatus.FORBIDDEN);

        if (salvo.getLocations().size() > 5)
            return new ResponseEntity<>(makeMap("Error", "you cannot send more than 5 salvoes"), HttpStatus.FORBIDDEN);

        if (getGamePlayer.getSalvoes().stream().anyMatch(turn -> turn.getTurnPlayer() == salvo.getTurnPlayer()))
            return new ResponseEntity<>(makeMap("Error", "exist"), HttpStatus.FORBIDDEN);

        if (getGamePlayer.getSalvoes().size() + 1 != salvo.getTurnPlayer())
            return new ResponseEntity<>(makeMap("Error", "shifts cannot be skipped"), HttpStatus.FORBIDDEN);

        Optional<GamePlayer> opponent = getGamePlayer.gamePlayerOpponent();

        if (opponent.isPresent()) {
            GamePlayer getOpponent = opponent.get();
            if (getGamePlayer.getSalvoes().size() - getOpponent.getSalvoes().size() >= 1) {
                return new ResponseEntity<>(makeMap("Error", "you must wait for the other player to shoot"), HttpStatus.FORBIDDEN);
            } else {
                Salvo salvoCurrent = new Salvo(salvo.getTurnPlayer(), getGamePlayer, salvo.getLocations());
                salvoRepository.save(salvoCurrent);

                getGamePlayer.getSalvoes().add(salvoCurrent);
                if (getGamePlayer.gamePlayerOpponent().isPresent()) {
                    if (getGamePlayer.getStatus() == GameStatus.TIE) {

                        scoreRepository.save(new Score(getGamePlayer.getGame(), getGamePlayer.getPlayer(), 0.5, LocalDateTime.now()));
                        scoreRepository.save(new Score(getGamePlayer.gamePlayerOpponent().get().getGame(), getGamePlayer.gamePlayerOpponent().get().getPlayer(), 0.5, LocalDateTime.now()));

                    } else if (getGamePlayer.getStatus() == GameStatus.WIN) {

                        scoreRepository.save(new Score(getGamePlayer.getGame(), getGamePlayer.getPlayer(), 1, LocalDateTime.now()));
                        scoreRepository.save(new Score(getGamePlayer.gamePlayerOpponent().get().getGame(), getGamePlayer.gamePlayerOpponent().get().getPlayer(), 0, LocalDateTime.now()));

                    } else if (getGamePlayer.getStatus()== GameStatus.LOSE) {

                        scoreRepository.save(new Score(getGamePlayer.getGame(), getGamePlayer.getPlayer(), 0, LocalDateTime.now()));
                        scoreRepository.save(new Score(getGamePlayer.gamePlayerOpponent().get().getGame(), getGamePlayer.gamePlayerOpponent().get().getPlayer(), 1, LocalDateTime.now()));
                    }
                }
                return new ResponseEntity<>(makeMap("Yes", "Created"), HttpStatus.CREATED);
            }
        } else {
            if (salvo.getTurnPlayer() == 1) {
                salvoRepository.save(new Salvo(salvo.getTurnPlayer(), getGamePlayer, salvo.getLocations()));
                return new ResponseEntity<>(makeMap("Yes", "Created"), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(makeMap("Error", "You can't shoot any more safely because you still don't have an opponent"), HttpStatus.FORBIDDEN);
            }
        }

    }

    /* GAME VIEW: Muestra los datos de un jugador segun la variable de ruta. */

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> findGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication) {

        if (isGuest(authentication))
            return new ResponseEntity<>(makeMap("Error", "you are not authenticated"), HttpStatus.FORBIDDEN);

        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);

        if (gamePlayer.isEmpty())
            return new ResponseEntity<>(makeMap("Error", "there is no game"), HttpStatus.NOT_FOUND);

        GamePlayer getGamePlayer = gamePlayer.get();
        Player authPlayer = playerRepository.findByUserName(authentication.getName());

        if (authPlayer.getId() != getGamePlayer.getPlayer().getId())
            return new ResponseEntity<>(makeMap("Error", "you can't see this resource"), HttpStatus.FORBIDDEN);


        return new ResponseEntity<>(makeGameViewDTO(gamePlayer.get()), HttpStatus.CREATED);

    }

    // METODOS AUXILIARES

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    /* DTOS */

    public Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("date", game.getDateOfCreate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(this::makeGamePlayerDTO).collect(toList()));
        return dto;
    }

    public Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        dto.put("score", gamePlayer.getScore().map(Score::getScore).orElse(null));
        return dto;
    }

    public Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("email", player.getUserName());
        return dto;
    }

    public Map<String, Object> makeGameViewDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getDateOfCreate());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(this::makeGamePlayerDTO).collect(toList()));
        dto.put("ships", gamePlayer.getShips().stream().map(this::makeShipDTO).collect(toList()));
        dto.put("salvoes", gamePlayer.getGame().getGamePlayers().stream().flatMap(g -> g.getSalvoes().stream().map(this::makeSalvoDTO)));
        dto.put("hits", gamePlayer.getSalvoes().stream().map(this::makeHitsDTO).collect(toList()));
        if (gamePlayer.gamePlayerOpponent().isPresent()) {
            dto.put("opponentHits", gamePlayer.gamePlayerOpponent().get().getSalvoes().stream().map(this::makeHitsDTO).collect(toList()));
        }
        dto.put("state", gamePlayer.getStatus());
        return dto;
    }

    public Map<String, Object> makeShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getType());
        dto.put("locations", ship.getLocations());
        return dto;
    }

    public Map<String, Object> makeSalvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", salvo.getTurnPlayer());
        dto.put("player", salvo.getGamePlayer().getPlayer().getId());
        dto.put("locations", salvo.getLocations());
        return dto;
    }

    public Map<String, Object> makeHitsDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", salvo.getTurnPlayer());
        dto.put("hits", salvo.getHits());
        dto.put("sunks", salvo.getSunks().stream().map(this::makeShipDTO));
        return dto;
    }

}
