package com.jakethomson.tictactoe.repositories;

import com.jakethomson.tictactoe.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    Game findById(int id);
}
