package com.jakethomson.tictactoe.controllers;

import com.jakethomson.tictactoe.models.Game;
import com.jakethomson.tictactoe.repositories.GameRepository;
import com.jakethomson.tictactoe.services.MoveService;
import com.jakethomson.tictactoe.services.ValidationService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** Handles the endpoints for all actions involving client moves. */
@RestController
public class MoveController {

    // Dependency injection.
    private final MoveService moveService;
    private final GameRepository repository;
    private final ValidationService validationService;
    public MoveController(MoveService moveService, GameRepository repository, ValidationService validationService) {
        this.moveService = moveService;
        this.repository = repository;
        this.validationService = validationService;
    }

    /* When a POST request to /games/{id} is made, the game with the
       matching id is updated using the information in the request body. */
    @PostMapping("/games/{id}")
    public Game move(@PathVariable long id,
                     @RequestBody Map<String, String[]> body) {
        Game game = repository.findById(id);
        char playerSide;
        if(game.getPlayer_x_id() == "Server")
            playerSide = 'O';
        else
            playerSide = 'X';
        if(validationService.validateMoveInput(game.getBoard(), body.get("board"), playerSide))
            return moveService.move(game, body.get("board"));
        else
            return game;
    }
}
