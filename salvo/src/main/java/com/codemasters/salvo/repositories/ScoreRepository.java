package com.codemasters.salvo.repositories;

import com.codemasters.salvo.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score,Long> {
}
