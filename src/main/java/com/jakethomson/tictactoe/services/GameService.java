package com.jakethomson.tictactoe.services;
import com.jakethomson.tictactoe.models.Game;

import com.jakethomson.tictactoe.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/** GameService handles all the logic for the creation and reference of new games.
 *  It also saves and loads game data to/from the repository. */
@Service
public class GameService {

    // Dependency injection.
    private final GameRepository repository;
    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new Game object and save it to the repository.
     *
     * @param player_x_id - States what player is X's, if it is the server, then
     *                      the first move needs to be performed.
     * @param player_o_id - States what player is O's.
     * @return the new state of the {@link Game}.
     */
    public Game newGame(String player_x_id, String player_o_id) {
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
