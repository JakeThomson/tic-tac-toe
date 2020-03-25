package com.jakethomson.tictactoe.repositories;

import com.jakethomson.tictactoe.models.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameRepository extends CrudRepository<Game, Long> {
    Game findById(long id);
}
