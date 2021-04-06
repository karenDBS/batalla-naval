package com.codemasters.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayers;

    @ElementCollection
    @Column(name="location")
    private List<String> location = new ArrayList<>();

    public Ship() {
    }

    public Ship(String type, GamePlayer gamePlayers, List<String> location) {
        this.type = type;
        this.gamePlayers = gamePlayers;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GamePlayer getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(GamePlayer gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

}
