package com.jakethomson.tictactoe.services;

import com.jakethomson.tictactoe.models.Game;
import com.jakethomson.tictactoe.repositories.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class MoveService {

    private final GameRepository repository;
    private final GameService gameService;

    public MoveService(GameRepository repository, GameService gameService) {
        this.repository = repository;
        this.gameService = gameService;
    }

    public Game move(long id, String[] board) {
        Game game = repository.findById(id);
        game.setBoard(board);

        repository.save(game);

        return game;
    }
}
