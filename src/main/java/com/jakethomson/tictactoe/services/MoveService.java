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
    private final GameService gameService;
    private final ServerMoveService serverMoveService;

    public MoveService(GameRepository repository, ServerMoveService serverMoveService, GameService gameService) {
        this.repository = repository;
        this.serverMoveService = serverMoveService;
        this.gameService = gameService;
    }



    /**
     * Creates a new Game object and save it to the repository.
     *
     * @param game - The id of the game to send a move to.
     * @param board - The new state of the board with the move to be made placed.
     * @return the new state of the {@link Game}, after the server has made it's next turn.
     */
    public Game move(Game game, String[] board) {
        game.setBoard(board);
        String serverSide, playerSide;
        if(game.getPlayer_x_id().equals("Server")) {
            serverSide = "X";
            playerSide = "O";
        } else {
            serverSide = "O";
            playerSide = "X";
        }

        if(!checkForEnd(board, game, playerSide)) {
            serverMoveService.makeBestMove(game.getBoard(), serverSide);
            checkForEnd(board, game, serverSide);
        }

        repository.save(game);

        return game;
    }

    public boolean checkForEnd(String[] board, Game game, String side) {
        int status = serverMoveService.checkGameStatus(board, side);
        if(status == +10) {
            game.setStatus(side);
            return true;
        } else if(status == -10) {
            if(side.equals("X"))
                game.setStatus("O");
            else if(side.equals("O"))
                game.setStatus("X");
            return true;
        } else if(status == 0 && serverMoveService.getRemainingMoves(board).equals("")) {
            game.setStatus("DRAW");
            return true;
        }
        return false;
    }
}
