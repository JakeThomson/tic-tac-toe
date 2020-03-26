package com.jakethomson.tictactoe.controllers;

import com.jakethomson.tictactoe.models.Game;
import com.jakethomson.tictactoe.services.MoveService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Handles the endpoints for all actions involving client moves. */
@RestController
public class MoveController {

    // Dependency injection.
    private final MoveService moveService;
    public MoveController(MoveService moveService) {
        this.moveService = moveService;
    }

    /* When a POST request to /games/{id} is made, the game with the
       matching id is updated using the information in the request body. */
    @PostMapping("/games/{id}")
    public Game move(@PathVariable long id,
                     @RequestBody Map<String, String[]> body) {
        return moveService.move(id, body.get("board"));
    }
}
