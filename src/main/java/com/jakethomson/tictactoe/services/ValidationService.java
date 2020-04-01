package com.jakethomson.tictactoe.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** ValidationService handles the logic for validating input data. */
@Service
public class ValidationService {
    public boolean validateMoveInput(String[] currentBoard, String[] newBoard, char playerSide) {
        // Ensure only one change has been made to the board.
        int changeCount = 0;
        for(int pos=0; pos < 9; pos++){
            int row = pos/3;
            int col = pos%3;

            // If there has been a change at this position.
            if(currentBoard[row].charAt(col) != newBoard[row].charAt(col)){
                // If the player tries to remove/modify a symbol that has already been placed.
                if(currentBoard[row].charAt(col) != ' ' && newBoard[row].charAt(col) == ' '){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot remove already placed symbols.");
                } else if(currentBoard[row].charAt(col) != ' ' && newBoard[row].charAt(col) != currentBoard[row].charAt(col)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot make changes to already placed symbols.");
                }

                // If the symbol placed was not the player's symbol.
                if(newBoard[row].charAt(col) != playerSide){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only make a move using your own symbol.");
                }
                changeCount++;
            }
        }

        // If more than one move has been made or if no moves have been made.
        if(changeCount > 1 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must only make one change to the board.");
        } else if(changeCount == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must make at least one change to the board.");
        }
        return true;
    }
}
