package com.jakethomson.tictactoe.services;

import org.springframework.stereotype.Service;

import java.util.Random;

/** MoveService handles all the logic and implementation of the server moves. */
@Service
public class ServerMoveService {

    private static final Random random = new Random();
    private static final int TOP_LEFT = 0;
    private static final int TOP_CENTRE = 1;
    private static final int TOP_RIGHT = 2;
    private static final int MID_LEFT = 3;
    private static final int MID_CENTRE = 4;
    private static final int MID_RIGHT = 5;
    private static final int BOT_LEFT = 6;
    private static final int BOT_CENTRE = 7;
    private static final int BOT_RIGHT = 8;

    /**
     * Creates the board and makes the first move.
     * Always one of the corners as that has the highest chance of winning.
     *
     * @return a board with the first move made.
     */
    public String[] firstMove() {
        int randomInt = random.nextInt(4);
        String[] board = new String[]{"   ", "   ", "   "};

        switch(randomInt) {
            case 0:
                makeMove(board, TOP_LEFT);
                break;
            case 1:
                makeMove(board, TOP_RIGHT);
                break;
            case 2:
                makeMove(board, BOT_LEFT);
                break;
            case 3:
                makeMove(board, BOT_RIGHT);
                break;
        }

        return board;
    }

    /**
     * Locates the char in the string array that the move is trying to replace, and replaces it with the server's symbol.
     */
    public void makeMove(String[] board, int position) {
        int topMidBot = position/3;
        int leftCenRight = position%3;

        board[topMidBot] = board[topMidBot].substring(0, leftCenRight) +"X"+ board[topMidBot].substring(leftCenRight+1);
    }
}
