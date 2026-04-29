package app;

import java.util.ArrayList;
import java.util.List;

public class Zuggenerator {

    public List<String> getAllLegalMoves(char[][] board){

        List<String> possibleMoves = new ArrayList<>();

        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){

                char currentFigure = board[i][j];
                switch (currentFigure){
                    case '-' -> {
                        break;
                    }
                    case 'w' -> {

                    }
                }

            }
        }
    }

    private void addMovesForWhiteFigure(char[][] board, int i, int j, boolean isWhite)

}
