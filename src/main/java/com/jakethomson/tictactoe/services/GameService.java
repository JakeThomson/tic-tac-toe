package com.jakethomson.tictactoe.services;
import com.jakethomson.tictactoe.models.Game;

import com.jakethomson.tictactoe.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GameService {

    private final GameRepository repository;

    GameService(GameRepository repository) {
        this.repository = repository;
    }

    public Game newGame(String player_x_id, String player_o_id) {
        AtomicLong id = new AtomicLong();
        String[] board;

        if(player_x_id == "Server") {
            board = new String[]{"X  ", "   ", "   "};
        } else {
            board = new String[]{"   ", "   ", "   "};
        }

        Game game = new Game(player_x_id, player_o_id, board);

        repository.save(game);

        return game;
    }

    public Game getGame(long id){
        return repository.findById(id);
    }
}
