package com.codemasters.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime dateOfCreate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    public Game() {
    }

    public Game(LocalDateTime dateOfCreate) {
        this.dateOfCreate = dateOfCreate;
    }

    public List<Player> getPlayers() {
        return gamePlayers.stream().map(GamePlayer::getPlayer).collect(toList());
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public LocalDateTime getDateOfCreate() {
        return dateOfCreate;
    }

    public long getId() {
        return id;
    }

    public void addGamePlayer(GamePlayer player){
        player.setGame(this);
        gamePlayers.add(player);
    }

}
