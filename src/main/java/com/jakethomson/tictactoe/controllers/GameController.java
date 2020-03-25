package com.jakethomson.tictactoe.controllers;

import com.jakethomson.tictactoe.models.Game;
import com.jakethomson.tictactoe.services.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/games")
    public Game newGame(@RequestParam(value = "name", defaultValue = "Player") String name,
                        @RequestParam(value = "side", defaultValue = "X") String side) {

        if(side.toUpperCase().equals("X")) {
            return gameService.newGame(name, "Server");
        } else {
            return gameService.newGame("Server", name);
        }
    }

    @GetMapping("/games/{game_id}")
        Game getGame(@PathVariable long id) {
        return gameService.getGame(id);
    }
}
