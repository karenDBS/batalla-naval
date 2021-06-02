package com.codemasters.salvo;

import com.codemasters.salvo.models.*;
import com.codemasters.salvo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(PlayerRepository repository, GameRepository repositoryGame, GamePlayerRepository repositoryGamePlayer, ShipRepository repositoryShip, SalvoRepository repositorySalvo, ScoreRepository scoreRepository) {
		return (args) -> {


			Player playerUno = new Player("j.bauer@ctu.gov",passwordEncoder.encode("24"));
			Player playerDos = new Player("c.obrian@ctu.gov",passwordEncoder.encode("42"));
			Player playerTres = new Player("kim_bauer@gmail.com",passwordEncoder.encode("kb"));
			Player playerCuatro = new Player("t.almeida@ctu.gov",passwordEncoder.encode("mole"));

			// save a couple of customers
			repository.save(playerUno);
			repository.save(playerDos);
			repository.save(playerTres);
			repository.save(playerCuatro);

			LocalDateTime dateTime = LocalDateTime.now();

			Game gameUno = new Game(dateTime);
			Game gameDos = new Game(dateTime.plusHours(1));
			Game gameTres = new Game(dateTime.plusHours(2));
			Game gameCuatro = new Game(dateTime.plusHours(2));

			repositoryGame.save(gameUno);
			repositoryGame.save(gameDos);
			repositoryGame.save(gameTres);
			repositoryGame.save(gameCuatro);

			GamePlayer gamePlayerUno = new GamePlayer(dateTime,gameUno,playerUno);
			GamePlayer gamePlayerDos = new GamePlayer(dateTime,gameUno,playerDos);
			GamePlayer gamePlayerTres = new GamePlayer(dateTime,gameDos,playerTres);
			GamePlayer gamePlayerCuatro = new GamePlayer(dateTime,gameDos,playerCuatro);
			GamePlayer gamePlayerCinco = new GamePlayer(dateTime,gameTres,playerCuatro);
			GamePlayer gamePlayerSeis = new GamePlayer(dateTime,gameCuatro,playerUno);
			GamePlayer gamePlayerSiete = new GamePlayer(dateTime,gameCuatro,playerTres);

			repositoryGamePlayer.save(gamePlayerUno);
			repositoryGamePlayer.save(gamePlayerDos);
			repositoryGamePlayer.save(gamePlayerTres);
			repositoryGamePlayer.save(gamePlayerCuatro);
			repositoryGamePlayer.save(gamePlayerCinco);
			repositoryGamePlayer.save(gamePlayerSeis);
			repositoryGamePlayer.save(gamePlayerSiete);

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

			Ship shipVeintiuno = new Ship("Carrier", gamePlayerSeis, List.of("F4","F5","F6","F7"));
			Ship shipVeintidos = new Ship("Submarine", gamePlayerSeis, List.of("A2", "A3", "A4"));
			Ship shipVientitres = new Ship("Battleship", gamePlayerSeis, List.of("I1","I2","I3","I4"));
			Ship shipVeinticuatro = new Ship("Destroyer", gamePlayerSeis, List.of("B5", "C5", "D5"));
			Ship shipVeinticinco = new Ship("Patrol Boat", gamePlayerSeis, List.of("C6", "C7"));

			Ship shipVeintiseis = new Ship("Carrier", gamePlayerSiete, List.of("D4","D5","D6","D7"));
			Ship shipVeintisiete = new Ship("Submarine", gamePlayerSiete, List.of("F1","F2","F3"));
			Ship shipVeintiocho = new Ship("Battleship", gamePlayerSiete, List.of("I1","I2","I3","I4"));
			Ship shipVeintinueve = new Ship("Submarine", gamePlayerSiete, List.of("F5","G5","H5"));
			Ship shipTreinta = new Ship("Patrol Boat", gamePlayerSiete, List.of("F6","F7"));

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
			repositoryShip.save(shipVeintiuno);
			repositoryShip.save(shipVeintidos);
			repositoryShip.save(shipVientitres);
			repositoryShip.save(shipVeinticuatro);
			repositoryShip.save(shipVeinticinco);
			repositoryShip.save(shipVeintiseis);
			repositoryShip.save(shipVeintisiete);
			repositoryShip.save(shipVeintiocho);
			repositoryShip.save(shipVeintinueve);
			repositoryShip.save(shipTreinta);

			Salvo salvoUno = new Salvo(1,gamePlayerUno,List.of("B5", "C5", "F1"));
			Salvo salvoDos = new Salvo(1,gamePlayerDos,List.of("B4", "B5", "B6"));
			Salvo salvoTres = new Salvo(2,gamePlayerUno,List.of("F2", "D5"));
			Salvo salvoCuatro = new Salvo(2,gamePlayerDos,List.of("E1", "H3", "A2"));
			Salvo salvoCinco = new Salvo(1,gamePlayerTres,List.of("A2", "A4", "G6"));
			Salvo salvoSeis = new Salvo(1,gamePlayerCuatro,List.of("B5", "D5", "C7"));
			Salvo salvoSiete = new Salvo(2,gamePlayerTres,List.of("A3", "H6"));
			Salvo salvoOcho = new Salvo(2,gamePlayerCuatro,List.of("C5", "C6"));
			Salvo salvoNueve = new Salvo(1,gamePlayerCinco,List.of("H1", "H2", "H3"));
			Salvo salvoDiez = new Salvo(1,gamePlayerSeis,List.of("H1", "H2", "H3"));
			Salvo salvoOnce = new Salvo(1,gamePlayerSiete,List.of("B4", "B5", "B6"));

			repositorySalvo.save(salvoUno);
			repositorySalvo.save(salvoDos);
			repositorySalvo.save(salvoTres);
			repositorySalvo.save(salvoCuatro);
			repositorySalvo.save(salvoCinco);
			repositorySalvo.save(salvoSeis);
			repositorySalvo.save(salvoSiete);
			repositorySalvo.save(salvoOcho);
			repositorySalvo.save(salvoNueve);
			repositorySalvo.save(salvoDiez);
			repositorySalvo.save(salvoOnce);

            Score scoreUno = new Score(gameUno,playerUno,1,dateTime);
			Score scoreDos = new Score(gameUno,playerDos,0,dateTime);
			Score scoreTres = new Score(gameDos,playerTres,0.5,dateTime);
			Score scoreCuatro = new Score(gameDos,playerCuatro,0.5,dateTime);
			Score scoreCinco = new Score(gameCuatro,playerUno,1,dateTime);
			Score scoreSeis = new Score(gameCuatro,playerTres,0,dateTime);

			scoreRepository.save(scoreUno);
			scoreRepository.save(scoreDos);
			scoreRepository.save(scoreTres);
			scoreRepository.save(scoreCuatro);
			scoreRepository.save(scoreCinco);
			scoreRepository.save(scoreSeis);

		};
	}

}


@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/web/game.html","/rest/**","/api/game_view/**").hasAuthority("USER")
				.antMatchers("/**").permitAll();

		http.formLogin()
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		http.csrf().disable();

		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}

	}
}


