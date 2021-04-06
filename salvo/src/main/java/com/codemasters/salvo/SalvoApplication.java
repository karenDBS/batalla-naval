package com.codemasters.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;

@SpringBootApplication
public class SalvoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository,GameRepository repositoryGame, GamePlayerRepository repositoryGamePlayer) {
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

			GamePlayer gamePlayerUno = new GamePlayer(gameUno,playerUno);
			GamePlayer gamePlayerDos = new GamePlayer(gameDos,playerDos);
			GamePlayer gamePlayerTres = new GamePlayer(gameDos,playerTres);
			GamePlayer gamePlayerCuatro = new GamePlayer(gameUno,playerCuatro);
			GamePlayer gamePlayerCinco = new GamePlayer(gameTres,playerTres);

			repositoryGamePlayer.save(gamePlayerUno);
			repositoryGamePlayer.save(gamePlayerDos);
			repositoryGamePlayer.save(gamePlayerTres);
			repositoryGamePlayer.save(gamePlayerCuatro);
			repositoryGamePlayer.save(gamePlayerCinco);

		};
	};

}



