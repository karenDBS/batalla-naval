package com.codemasters.salvo.repositories;

import com.codemasters.salvo.models.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship,String> {

}
