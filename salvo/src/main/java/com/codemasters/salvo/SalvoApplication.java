package com.codemasters.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository,GameRepository repositoryGame, GamePlayerRepository repositoryGamePlayer, ShipRepository repositoryShip) {
		return (args) -> {

			Player playerUno = new Player("j.bauer@ctu.gov");
			Player playerDos = new Player(" c.obrian@ctu.gov");
			Player playerTres = new Player("kim_bauer@gmail.com");
			Player playerCuatro = new Player("t.almeida@ctu.gov");

			// save a couple of customers
			repository.save(playerUno);
			repository.save(playerDos);
			repository.save(playerTres);
			repository.save(playerCuatro);

			LocalDateTime dateTime = LocalDateTime.now();

			Game gameUno = new Game(dateTime);
			Game gameDos = new Game(dateTime.plusHours(1));
			Game gameTres = new Game(dateTime.plusHours(2));

			repositoryGame.save(gameUno);
			repositoryGame.save(gameDos);
			repositoryGame.save(gameTres);

			GamePlayer gamePlayerUno = new GamePlayer(dateTime,gameUno,playerUno);
			GamePlayer gamePlayerDos = new GamePlayer(dateTime,gameUno,playerDos);
			GamePlayer gamePlayerTres = new GamePlayer(dateTime,gameDos,playerTres);
			GamePlayer gamePlayerCuatro = new GamePlayer(dateTime,gameDos,playerCuatro);
			GamePlayer gamePlayerCinco = new GamePlayer(dateTime,gameTres,playerCuatro);

			repositoryGamePlayer.save(gamePlayerUno);
			repositoryGamePlayer.save(gamePlayerDos);
			repositoryGamePlayer.save(gamePlayerTres);
			repositoryGamePlayer.save(gamePlayerCuatro);
			repositoryGamePlayer.save(gamePlayerCinco);

			Ship shipUno = new Ship("Submarine", gamePlayerUno, List.of("F1", "G1", "H1"));
			Ship shipDos = new Ship("Carrier", gamePlayerUno, List.of("I4","I5","I6","I7"));
			Ship shipTres = new Ship("Battleship", gamePlayerUno, List.of("E2","E3","E4","E5"));
			Ship shipCuatro = new Ship("Patrol Boat", gamePlayerUno, List.of("B9", "B10"));
			Ship shipCinco = new Ship("Destroyer", gamePlayerUno, List.of("B7", "C7", "D7"));

			Ship shipSeis = new Ship("Carrier", gamePlayerDos, List.of("D4","D5","D6","D7"));
			Ship shipSiete = new Ship("Submarine", gamePlayerDos, List.of("F1","F2","F3"));
			Ship shipOcho = new Ship("Battleship", gamePlayerDos, List.of("I1","I2","I3","I4"));
			Ship shipNueve = new Ship("Submarine", gamePlayerDos, List.of("F5","G5","H5"));
			Ship shipDiez = new Ship("Patrol Boat", gamePlayerDos, List.of("F6","F7"));

			Ship shipOnce = new Ship("Submarine", gamePlayerTres, List.of("A2", "A3", "A4"));
			Ship shipDoce = new Ship("Carrier", gamePlayerTres, List.of("I4","I5","I6","I7"));
			Ship shipTrece = new Ship("Battleship", gamePlayerTres, List.of("E2","E3","E4","E5"));
			Ship shipCatorce = new Ship("Patrol Boat", gamePlayerTres, List.of("G6", "H6"));
			Ship shipQuince = new Ship("Destroyer", gamePlayerTres, List.of("B5", "C5", "D5"));

			Ship shipDieciseis = new Ship("Carrier", gamePlayerCuatro, List.of("F4","F5","F6","F7"));
			Ship shipDiecisiete = new Ship("Submarine", gamePlayerCuatro, List.of("A2", "A3", "A4"));
			Ship shipDieciocho = new Ship("Battleship", gamePlayerCuatro, List.of("I1","I2","I3","I4"));
			Ship shipDiecinueve = new Ship("Destroyer", gamePlayerCuatro, List.of("B5", "C5", "D5"));
			Ship shipVeinte = new Ship("Patrol Boat", gamePlayerCuatro, List.of("C6", "C7"));


			repositoryShip.save(shipUno);
			repositoryShip.save(shipDos);
			repositoryShip.save(shipTres);
			repositoryShip.save(shipCuatro);
			repositoryShip.save(shipCinco);
			repositoryShip.save(shipSeis);
			repositoryShip.save(shipSiete);
			repositoryShip.save(shipOcho);
			repositoryShip.save(shipNueve);
			repositoryShip.save(shipDiez);
			repositoryShip.save(shipOnce);
			repositoryShip.save(shipDoce);
			repositoryShip.save(shipTrece);
			repositoryShip.save(shipCatorce);
			repositoryShip.save(shipQuince);
			repositoryShip.save(shipDieciseis);
			repositoryShip.save(shipDiecisiete);
			repositoryShip.save(shipDieciocho);
			repositoryShip.save(shipDiecinueve);
			repositoryShip.save(shipVeinte);

		};
	};

}



