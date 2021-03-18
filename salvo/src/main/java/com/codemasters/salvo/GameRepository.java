package com.codemasters.salvo;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game,Long> {
    List<Player> findBydateOfCreate(Date dateOfCraete);
}
