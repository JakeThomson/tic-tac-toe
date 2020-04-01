package com.jakethomson.tictactoe.services;

import org.springframework.stereotype.Service;
import com.jakethomson.tictactoe.models.Game;
import java.util.Random;

import static java.lang.Math.*;

/** MoveService handles all the logic and implementation of the server moves. */
@Service
public class ServerMoveService {

    private static final Random random = new Random();
    private static final int TOP_LEFT = 0;
    private static final int TOP_RIGHT = 2;
    private static final int BOT_LEFT = 6;
    private static final int BOT_RIGHT = 8;

    private char serverSide;

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
                makeMove(board, TOP_LEFT, "X");
                break;
            case 1:
                makeMove(board, TOP_RIGHT, "X");
                break;
            case 2:
                makeMove(board, BOT_LEFT, "X");
                break;
            case 3:
                makeMove(board, BOT_RIGHT, "X");
                break;
        }

        return board;
    }

    /**
     * Locates the char in the string array that the move is trying to replace,
     * and replaces it with the server's symbol.
     */
    public void makeMove(String[] board, int position, String side) {
        int row = position/3;
        int col = position%3;

        board[row] = board[row].substring(0, col) + side + board[row].substring(col+1);
    }


    public int checkGameStatus(String[] board, String side) {

        // Check for horizontal wins.
        for(int row = 0; row < 3; row++) {
            if(board[row].charAt(0) == board[row].charAt(1) &&
                    board[row].charAt(1) == board[row].charAt(2) &&
                    board[row].charAt(0) != ' ' ) {
                if(board[row].charAt(0) == side.charAt(0)) {
                    return +10;
                }
                else {
                    return -10;
                }
            }
        }

        // Check for vertical wins.
        for(int col = 0; col < 3; col++) {
            if (board[0].charAt(col) == board[1].charAt(col) &&
                    board[1].charAt(col) == board[2].charAt(col) &&
                    board[0].charAt(col) != ' ' ) {
                if (board[0].charAt(col) == side.charAt(0)) {
                    return +10;
                } else {
                    return -10;
                }
            }
        }

        // Check for diagonal wins
        if(board[0].charAt(0) == board[1].charAt(1) &&
                board[1].charAt(1) == board[2].charAt(2) &&
                board[0].charAt(0) != ' ' ) {
            if(board[0].charAt(0) == side.charAt(0)) {
                return +10;
            }
            else {
                return -10;
            }
        }
        if(board[0].charAt(2) == board[1].charAt(1) &&
                board[1].charAt(1) == board[2].charAt(0) &&
                board[2].charAt(0) != ' ' ) {
            if(board[0].charAt(2) == side.charAt(0)) {
                return +10;
            }
            else {
                return -10;

            }
        }

        return 0;
    }

    public String  getRemainingMoves(String[] board) {
        StringBuilder remaining = new StringBuilder();
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                if(board[row].substring(col, col+1).equals(" ")) {
                    remaining.append(row * 3 + col);
                }
            }
        }
        return remaining.toString();
    }

    public void makeBestMove(String[] board, String side){
        int bestMove = 0;
        int bestVal = -1000;
        serverSide = side.charAt(0);
        char playerSide;
        if(serverSide == 'X')
            playerSide = 'O';
        else
            playerSide = 'X';

        String remainingMoves = getRemainingMoves(board);
        for(int i = 0; i < remainingMoves.length(); i++) {
            int pos = Character.getNumericValue(remainingMoves.charAt(i));
            makeMove(board, pos, side);
            String newRemaining = getRemainingMoves(board);
            String nextSide;
            if (side.equals("O"))
                nextSide = "X";
            else
                nextSide = "O";
            int moveVal = miniMax(board, 0, newRemaining, pos, nextSide, false);
            makeMove(board, pos, " ");

            if(moveVal > bestVal) {
                bestVal = moveVal;
                bestMove = pos;
            }
        }
        makeMove(board, bestMove, side);
    }

    public int miniMax(String[] board, int depth, String remaining, int pos, String side, boolean isMaximizingPlayer) {

        int score = checkGameStatus(board, side);

        if(score == 10 || score == -10) {
            return score;
        }
        else if(remaining.equals("")) {
            return 0;
        }

        if(isMaximizingPlayer) {
            int bestVal = -1000;
            for(int i = 0; i < 9; i++) {
                if(remaining.contains(Integer.toString(i))) {
                    makeMove(board, i, side);
                    String newRemaining = getRemainingMoves(board);
                    String nextSide;
                    if (side.equals("X"))
                        nextSide = "O";
                    else
                        nextSide = "X";
                    bestVal = max(bestVal, miniMax(board, depth + 1, newRemaining, pos, nextSide, !isMaximizingPlayer));
                    makeMove(board, i, " ");
                }
            }
            return bestVal;
        }
        else {
            int bestVal = 1000;
            for(int i = 0; i < 9; i++) {
                if(remaining.contains(Integer.toString(i))) {
                    makeMove(board, i, side);
                    String newRemaining = getRemainingMoves(board);
                    String nextSide;
                    if (side.equals("O"))
                        nextSide = "X";
                    else
                        nextSide = "O";
                    bestVal = min(bestVal, miniMax(board, depth + 1, newRemaining, pos, nextSide, !isMaximizingPlayer));
                    makeMove(board, i, " ");
                }
            }
            return bestVal;
        }
    }
}
