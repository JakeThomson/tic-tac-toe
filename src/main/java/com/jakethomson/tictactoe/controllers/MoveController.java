package com.jakethomson.tictactoe.controllers;

import com.jakethomson.tictactoe.models.Game;
import com.jakethomson.tictactoe.services.MoveService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoveController {
    private final MoveService moveService;

    public MoveController(MoveService moveService) {
        this.moveService = moveService;
    }

    @PostMapping("/games/{id}")
    public Game move(@PathVariable long id,
                     @RequestBody String[] board) {
        return moveService.move(id, board);
    }
}
