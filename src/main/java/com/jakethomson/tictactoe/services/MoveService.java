package com.jakethomson.tictactoe.services;

import com.jakethomson.tictactoe.models.Game;
import com.jakethomson.tictactoe.repositories.GameRepository;
import org.springframework.stereotype.Service;

/** MoveService handles all the logic for the creation and reference of new games.
 *  It also saves and loads updated game data to/from the repository. */
@Service
public class MoveService {

    // Dependency injection.
    private final GameRepository repository;
    private final ServerMoveService serverMoveService;

    public MoveService(GameRepository repository, ServerMoveService serverMoveService) {
        this.repository = repository;
        this.serverMoveService = serverMoveService;
    }

    /**
     * Creates a new Game object and save it to the repository.
     *
     * @param id - The id of the game to send a move to.
     * @param board - the new state of the board with the move to be made placed.
     * @return the new state of the {@link Game}, after the server has made it's next turn.
     */
    public Game move(long id, String[] board) {
        Game game = repository.findById(id);
        game.setBoard(board);

        serverMoveService.makeBestMove(game.getBoard(), "X");

        repository.save(game);

        return game;
    }
}
