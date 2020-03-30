package com.jakethomson.tictactoe.controllers;

import com.jakethomson.tictactoe.models.Game;
import com.jakethomson.tictactoe.services.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** Handles the endpoints for all actions involving game details. */
@RestController
public class GameController {

    // Dependency injection.
    private final GameService gameService;
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // When a POST request to /games is made, a new game is created.
    @PostMapping("/games")
    public Game newGame(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String side = body.get("side");

        if(side.toUpperCase().equals("X")) {
            return gameService.newGame(name, "Server");
        } else {
            return gameService.newGame("Server", name);
        }
    }

    /* Data of created games can be accessed at any point by sending a GET request
       to the /games/{id} endpoint. */
    @GetMapping("/games/{id}")
    public Game getGame(@PathVariable long id) {
        return gameService.getGame(id);
    }

}
