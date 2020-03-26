package com.jakethomson.tictactoe.models;

import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.concurrent.atomic.AtomicLong;

/** Game is a POJO that holds all data on a single game. */
@Entity
public class Game {

    // A unique ID is generated each time a new game is instantiated.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String player_x_id;
    private String player_o_id;
    private String[] board;

    // Constructor
    public Game(String player_x_id, String player_o_id, String[] board) {
        this.player_x_id = player_x_id;
        this.player_o_id = player_o_id;
        this.board = board;
    }

    // Getters and board setter.
    public long getId() { return id; }
    public String getPlayer_x_id() { return player_x_id; }
    public String getPlayer_o_id() { return player_o_id; }
    public String[] getBoard() { return board; }
    public void setBoard(String[] board) { this.board = board; }
}
