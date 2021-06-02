package com.codemasters.salvo.repositories;

import com.codemasters.salvo.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player,Long>{
    Player findByUserName(Object userName);
}
