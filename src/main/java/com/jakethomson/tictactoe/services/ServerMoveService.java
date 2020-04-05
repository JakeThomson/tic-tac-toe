package com.jakethomson.tictactoe.services;

import org.springframework.stereotype.Service;
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
     * and replaces it with the chosen symbol.
     */
    private void makeMove(String[] board, int position, String side) {
        int row = position/3;
        int col = position%3;

        board[row] = board[row].substring(0, col) + side + board[row].substring(col+1);
    }

    /**
     * Checks to see if the game is in a winning/draw state.
     *
     * @param board - A string array holding the contents of the current board.
     * @param side - The side that will be checked for win/lose.
     * @return a +10 if the side provided has won the game, -10 if it lost, or 0 for a draw.
     */
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

    /**
     * Check the board for positions available to make moves into.
     *
     * @param board - A string array holding the current information of the board.
     * @return a string of numbers that represent each position of the board that is free.
     */
    public String  getRemainingMoves(String[] board) {
        StringBuilder remaining = new StringBuilder();
        // Iterate through the rows and cols of the board.
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                // When the square is empty, append the position value onto the string.
                if(board[row].substring(col, col+1).equals(" ")) {
                    remaining.append(row * 3 + col);
                }
            }
        }
        return remaining.toString();
    }


    /**
     * Decides what the best move is, and makes that move.
     *
     * @param board - A string array holding the current information of the board.
     * @param serverSide - A string containing the side that the server is playing as.
     */
    public void makeBestMove(String[] board, String serverSide){
        int bestMove = 0;
        int bestVal = -1000;

        String remainingMoves = getRemainingMoves(board);
        // Iterate through the remaining moves left on the board.
        for(int i = 0; i < remainingMoves.length(); i++) {
            // Get the position of the empty space.
            int pos = Character.getNumericValue(remainingMoves.charAt(i));
            // Make the move.
            makeMove(board, pos, serverSide);
            // Reevaluate the moves available.
            String newRemaining = getRemainingMoves(board);
            // Find the Server's counter and call miniMax as the Player's turn.
            String nextSide;
            if (serverSide.equals("O"))
                nextSide = "X";
            else
                nextSide = "O";
            int moveVal = miniMax(board, 0, newRemaining, nextSide, serverSide, false);
            // Undo the move
            makeMove(board, pos, " ");

            // If this move resulted in a higher chance of a win, then set the best position as this one.
            if(moveVal > bestVal) {
                bestVal = moveVal;
                bestMove = pos;
            }
        }
        // Make the move to the best position on the board with the Server's counter.
        makeMove(board, bestMove, serverSide);
    }

    /**
     * A recursive algorithm that looks at all possible moves of the game and returns a score based on the outcome
     * of each option.
     *
     * @param board - A string array holding the current information of the board.
     * @param depth - How deep into the recursion the algorithm is.
     * @param remaining - A string that holds the positions of each remaining move available.
     * @param side - The side of the player who's 'turn' it is.
     * @param isMaximizingPlayer - Switches from true to false for every new recursive call.
     * @return the value of the best outcome.
     */
    private int miniMax(String[] board, int depth, String remaining, String side, String serverSide, boolean isMaximizingPlayer) {

        int score = checkGameStatus(board, serverSide);

        // If game has ended, return the score.
        if(score == 10 || score == -10) {
            return score;
        }
        else if(remaining.equals("")) {
            return 0;
        }


        // If is maximising player.
        if(isMaximizingPlayer) {
            int bestVal = -1000;
            // Iterate through the board and check to see if that position is available to make a move in.
            for(int i = 0; i < 9; i++) {
                if(remaining.contains(Integer.toString(i))) {
                    // Make the move.
                    makeMove(board, i, side);
                    // Reevaluate the moves available.
                    String newRemaining = getRemainingMoves(board);
                    // Swap the player's counter and call miniMax as the next turn.
                    String nextSide;
                    if (side.equals("X"))
                        nextSide = "O";
                    else
                        nextSide = "X";
                    bestVal = max(bestVal, miniMax(board, depth + 1, newRemaining, nextSide, serverSide, !isMaximizingPlayer));
                    // Undo the move.
                    makeMove(board, i, " ");
                }
            }
            return bestVal;
        }
        // If is not maximising player.
        else {
            int bestVal = 1000;
            // Iterate through the board and check to see if that position is available to make a move in.
            for(int i = 0; i < 9; i++) {
                if(remaining.contains(Integer.toString(i))) {
                    // Make the move.
                    makeMove(board, i, side);
                    // Reevaluate the moves available.
                    String newRemaining = getRemainingMoves(board);
                    // Swap the player's counter and call miniMax as the next turn.
                    String nextSide;
                    if (side.equals("O"))
                        nextSide = "X";
                    else
                        nextSide = "O";
                    bestVal = min(bestVal, miniMax(board, depth + 1, newRemaining, nextSide, serverSide, !isMaximizingPlayer));
                    // Undo the move.
                    makeMove(board, i, " ");
                }
            }
            return bestVal;
        }
    }
}
