package com.codemasters.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private int turnPlayer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="locations")
    private List<String> locations = new ArrayList<>();

    public Salvo() {
    }

    public Salvo(int turnPlayer, GamePlayer gamePlayer, List<String> locations) {
        this.turnPlayer = turnPlayer;
        this.gamePlayer = gamePlayer;
        this.locations = locations;
    }

    public long getId() {
        return id;
    }

    public int getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(int turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<String> getHits() {

        Optional<GamePlayer> opponent = gamePlayer.gamePlayerOpponent();
        List<String> locations = new ArrayList<>();

        if (opponent.isPresent()) {

            List<String> shipsOpponent = opponent.get().getShips().stream().flatMap(ship -> ship.getLocations().stream()).collect(toList());
            locations = shipsOpponent.stream().filter(this.locations::contains).collect(toList());

        }

        return locations;
    }

    public List<Ship> getSunks(){

        List<Ship> shipsSunks = new ArrayList<>();
        Optional<GamePlayer> opponent = gamePlayer.gamePlayerOpponent();
        if (opponent.isPresent()) {

            List<String> acertados = gamePlayer.getSalvoes().stream().filter(salvo-> salvo.turnPlayer <= this.turnPlayer).flatMap(salvo-> salvo.getHits().stream()).collect(toList());
            shipsSunks = opponent.get().getShips().stream().filter(ship -> acertados.containsAll(ship.getLocations())).collect(toList());

        }

        return shipsSunks;
    }


}


