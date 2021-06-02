package com.codemasters.salvo.repositories;

import com.codemasters.salvo.models.Salvo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalvoRepository extends JpaRepository<Salvo,Long> {
}
