package com.jakethomson.tictactoe.controllers;

import com.jakethomson.tictactoe.models.Game;
import com.jakethomson.tictactoe.services.MoveService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoveController {
//    private final MoveService moveService;
//
//    public MoveController(MoveService moveService) {
//        this.moveService = moveService;
//    }

//    @PostMapping("/games/{game_id}")
//    public Game move(@RequestBody String[] board) {
//        return moveService.move(board);
//    }
}
