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
    private Enum status;

    // Default constructor.
    // TODO: Understand why this is being required when loading from repository.
    public Game() {}

    // Constructor
    public Game(String player_x_id, String player_o_id, String[] board) {
        this.player_x_id = player_x_id;
        this.player_o_id = player_o_id;
        this.board = board;
        this.status = gameStatus.IN_PROGRESS;
    }

    public enum gameStatus{
        IN_PROGRESS, PLAYER_X_WIN, PLAYER_O_WIN, DRAW
    }

    // Getters and status/board setter.
    public long getId() { return id; }
    public String getPlayer_x_id() { return player_x_id; }
    public String getPlayer_o_id() { return player_o_id; }
    public String[] getBoard() { return board; }
    public Enum getStatus() { return status; }
    public void setBoard(String[] board) { this.board = board; }
    public void setStatus(String status) {
        switch(status){
            case "X":
                this.status = gameStatus.PLAYER_X_WIN;
                break;
            case "O":
                this.status = gameStatus.PLAYER_O_WIN;
                break;
            case "DRAW":
                this.status = gameStatus.DRAW;
                break;
        }
    }
}
