package com.codemasters.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime creation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @OneToMany(mappedBy="gamePlayers", fetch=FetchType.EAGER)
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Salvo> salvoes = new HashSet<>();

    public GamePlayer() {
    }

    public GamePlayer(LocalDateTime creation, Game game, Player player) {
        this.creation = creation;
        this.game = game;
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    public Optional<Score> getScore() {
        return player.getScore(this.game);
    }

    public void addShip(Ship ship){
        ship.setGamePlayers(this);
        ships.add(ship);
    }

    public void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        salvoes.add(salvo);
    }

    public Optional<GamePlayer> gamePlayerOpponent(){
        return game.getGamePlayers().stream().filter(player1 -> player1.getId() != this.getId()).findFirst();
    }


    public GameStatus getStatus() {

        Optional<GamePlayer> gamePlayerOppo = this.gamePlayerOpponent();

        if (this.getShips().isEmpty()) {
            return GameStatus.WAITING_YOU_SHIPS;
        } else {
            if (gamePlayerOppo.isPresent()) {
                if (gamePlayerOppo.get().getShips().isEmpty()) {
                    return GameStatus.WAITING_OPPONENT_SHIPS;
                } else {
                    if (this.getSalvoes().stream().noneMatch(salvo -> salvo.getTurnPlayer() == this.getSalvoes().size())) {
                        return GameStatus.WAITING_YOU_SALVOS;
                    } else {
                        if (gamePlayerOppo.get().getSalvoes().stream().noneMatch(x -> x.getTurnPlayer() == this.getSalvoes().size())) {
                            return GameStatus.WAITING_OPPONENT_SALVOS;
                        } else {

                            List<Long> mySunks = this.getSalvoes().stream().filter(x -> x.getTurnPlayer() == this.getSalvoes().size()).flatMap(x -> x.getSunks().stream()).map(Ship::getId).collect(toList());
                            List<Long> sunksOpponent = new ArrayList<>();
                            Optional<GamePlayer> gamePlayerOpponent = this.gamePlayerOpponent();

                            if (gamePlayerOpponent.isPresent()) {
                                sunksOpponent = gamePlayerOpponent.get().getSalvoes().stream().filter(x -> x.getTurnPlayer() == this.getSalvoes().size()).flatMap(x -> x.getSunks().stream()).map(Ship::getId).collect(toList());
                            }

                            if (mySunks.size() == 5 && sunksOpponent.size() == 5) {
                                return GameStatus.TIE;
                            } else if (sunksOpponent.size() == 5) {
                                return GameStatus.LOSE;
                            } else if (mySunks.size() == 5) {
                                return GameStatus.WIN;
                            }else{
                                return GameStatus.WAITING_YOU_SALVOS;
                            }
                        }
                    }
                }
            }else{
                return GameStatus.WAITING_OPPONENT;
            }
        }

    }

}

