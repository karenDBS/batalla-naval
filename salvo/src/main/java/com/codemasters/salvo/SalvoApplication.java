package com.codemasters.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDate;

@SpringBootApplication
public class SalvoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository , GameRepository repository) {
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

		};
	};

}



